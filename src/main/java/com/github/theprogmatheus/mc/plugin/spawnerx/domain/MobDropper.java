package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MobDropper {

    private static final ThreadLocalRandom rand = ThreadLocalRandom.current();


    public static void handleDefaultDrops(@NotNull EntityDeathEvent event, @NotNull MobEntity mob) {
        var entity = event.getEntity();
        var mobConfig = mob.getConfig();

        if (mobConfig.getDrops() != null)
            event.getDrops().clear();

        if (mobConfig.getDropExp() > 0)
            event.setDroppedExp(0);


        var amountUnstacked = mob.getLastUnstackAmount();
        if (amountUnstacked <= 0)
            return;

        if (!event.getDrops().isEmpty() && amountUnstacked > 1) {
            var drops = new ArrayList<>(event.getDrops());
            event.getDrops().clear();
            event.getDrops().addAll(multiplyDrops(drops, amountUnstacked));
        }

        var newExp = (event.getDroppedExp() * amountUnstacked);
        if (newExp > 0) {
            var killer = entity.getKiller();
            if (killer == null)
                event.setDroppedExp(newExp);
            else {
                killer.giveExp(newExp);
                event.setDroppedExp(0);
            }
        }
    }

    public static void dropAll(@NotNull MobEntity entity, @NotNull MobConfig config, @NotNull LivingEntity killer, long amount) {
        if (amount <= 0)
            return;

        var drops = config.getDrops();
        var loc = entity.getEntity().getLocation();

        if (drops != null) {
            for (MobDrop drop : drops) {
                long dropAmount = calculateDropAmount(drop, amount);

                if (drop instanceof MobDrop.MobDropItem item)
                    dropItem(item, loc, (long) (dropAmount * (1 + getLootingMultiplier(killer, item))));
                else if (drop instanceof MobDrop.MobDropCommand command)
                    runCommands(command, killer, amount, (int) dropAmount);
            }
        }

        int exp = config.getDropExp() * (int) amount;
        if (exp > 0)
            dropExp(killer, loc, exp);
    }

    private static List<ItemStack> multiplyDrops(List<ItemStack> baseDrops, long multiplier) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack drop : baseDrops)
            result.addAll(splitIntoMaxStacks(drop, drop.getAmount() * multiplier));

        return result;
    }

    private static void dropExp(@NotNull LivingEntity killer, @NotNull Location location, int exp) {
        if (killer instanceof Player player)
            player.giveExp(exp);
        else {
            var world = location.getWorld();
            var experienceOrb = world.spawn(location, ExperienceOrb.class);
            experienceOrb.setExperience(exp);
        }
    }

    private static double getLootingMultiplier(@NotNull LivingEntity killer, @NotNull MobDrop.MobDropItem item) {
        var multiplier = item.getLooting();
        if (multiplier > 0)
            return getLootingLevel(killer) * multiplier;
        return 0;
    }

    private static int getLootingLevel(@NotNull LivingEntity killer) {
        if (killer instanceof Player player) {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (weapon.containsEnchantment(Enchantment.LOOT_BONUS_MOBS))
                return weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
        }
        return 0;
    }

    private static long calculateDropAmount(@NotNull MobDrop drop, long amount) {
        if (amount <= 0)
            return 0;

        double dropChance = drop.getChance();
        if (dropChance <= 0)
            return 0;

        int min = 1, max = 1;
        if (drop instanceof MobDrop.MobDropItem item) {
            min = item.getMin();
            max = item.getMax();
        }
        double negativeNoise = (0 - drop.getNegativeNoise());
        double positiveNoise = drop.getPositiveNoise();
        double expectedPerDrop = (min + rand.nextInt(max - min + 1)) * dropChance;
        double expected = amount * expectedPerDrop;
        if (positiveNoise > negativeNoise)
            expected *= 1.0 + rand.nextDouble(negativeNoise, positiveNoise);

        var result = (long) expected;
        if (rand.nextDouble() < (expected % 1))
            result++;

        return Math.min(result, amount * max);
    }

    private static List<ItemStack> splitIntoMaxStacks(@NotNull ItemStack original, long amount) {
        if (amount <= 1)
            return List.of(original);

        List<ItemStack> items = new ArrayList<>();
        ItemStack itemStack;
        while (amount > Integer.MAX_VALUE) {
            itemStack = original.clone();
            itemStack.setAmount(Integer.MAX_VALUE);
            items.add(itemStack);
            amount -= Integer.MAX_VALUE;
        }
        itemStack = original.clone();
        itemStack.setAmount((int) amount);
        items.add(itemStack);
        return items;
    }

    private static void dropItem(@NotNull MobDrop.MobDropItem item, @NotNull Location location, long amountToDrop) {
        if (amountToDrop <= 0)
            return;

        var world = location.getWorld();
        if (world == null)
            return;

        splitIntoMaxStacks(item.getItem(), amountToDrop)
                .forEach(itemToDrop -> world.dropItemNaturally(location, itemToDrop));
    }


    private static void runCommands(@NotNull MobDrop.MobDropCommand commandDrop, @NotNull LivingEntity killer, long amount, long dropAmount) {
        if (dropAmount <= 0)
            return;

        for (String command : commandDrop.getCommands()) {
            String parsed = command
                    .replace("%player%", killer.getName())
                    .replace("%amount_killed%", String.valueOf(amount))
                    .replace("%amount%", String.valueOf(dropAmount));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsed);
        }
    }
}
