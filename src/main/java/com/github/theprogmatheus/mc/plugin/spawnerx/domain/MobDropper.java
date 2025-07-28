package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MobDropper {

    private static final ThreadLocalRandom rand = ThreadLocalRandom.current();

    public static void dropAll(@NotNull MobEntity entity, @NotNull MobConfig config, @NotNull LivingEntity killer, long amount) {
        if (amount <= 0)
            return;

        var drops = config.getDrops();
        var exp = config.getDropExp();
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
        if (exp > 0)
            dropExp(killer, loc, exp);
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
        double dropChance = drop.getChance();
        if (dropChance <= 0)
            return 0;

        int min = 1, max = 1;
        if (drop instanceof MobDrop.MobDropItem item) {
            min = item.getMin();
            max = item.getMax();
        }

        double expectedPerDrop = (min + rand.nextInt(max - min + 1)) * dropChance;
        double expected = amount * expectedPerDrop;
        expected *= 1.0 + rand.nextDouble(-drop.getNegativeNoise(), drop.getPositiveNoise());

        var result = (long) expected;
        if (rand.nextDouble() < (expected % 1))
            result++;

        return result;
    }

    private static void dropItem(@NotNull MobDrop.MobDropItem item, @NotNull Location location, long amountToDrop) {
        if (amountToDrop <= 0)
            return;

        var world = location.getWorld();
        List<ItemStack> items = new ArrayList<>();
        ItemStack itemStack;
        while (amountToDrop > Integer.MAX_VALUE) {
            itemStack = item.getItem().clone();
            itemStack.setAmount(Integer.MAX_VALUE);
            items.add(itemStack);

            amountToDrop -= Integer.MAX_VALUE;
        }
        itemStack = item.getItem().clone();
        itemStack.setAmount((int) amountToDrop);
        items.add(itemStack);

        items.forEach(itemToDrop -> world.dropItemNaturally(location, itemToDrop));
    }


    private static void runCommands(@NotNull MobDrop.MobDropCommand commandDrop, @NotNull LivingEntity killer, long amount, long dropAmount) {
        for (String command : commandDrop.getCommands()) {
            String parsed = command
                    .replace("%player%", killer.getName())
                    .replace("%amount_killed%", String.valueOf(amount))
                    .replace("%amount%", String.valueOf(dropAmount));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsed);
        }
    }
}
