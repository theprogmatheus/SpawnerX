package com.github.theprogmatheus.mc.plugin.spawnerx.listener.mobs;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.MobConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.MobEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDeathListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onMobDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled())
            return;

        if ((event.getEntity() instanceof LivingEntity entity)
                && (event.getDamage() >= entity.getHealth())) {

            var optional = MobEntity.fromEntity(entity);
            if (optional.isEmpty())
                return;

            var mob = optional.get();
            mob.simulateDeath(getKiller(event), 1); // 1 for test

            event.setCancelled(mob.getStackedAmount() > 0);
        }
    }


    @EventHandler
    void onMobDeath(EntityDeathEvent event) {
        var entity = event.getEntity();
        var mobEntityOptional = MobEntity.fromEntity(entity);

        mobEntityOptional.ifPresent(MobEntity::unlink);

        if (mobEntityOptional.isEmpty())
            mobEntityOptional = MobEntity.fromFakeEntity(entity);

        mobEntityOptional.ifPresent(mob -> {
            var mobConfig = MobConfig.getConfig(entity.getType());
            if (mobConfig == null)
                return;

            var drops = mobConfig.getDrops();
            var exp = mobConfig.getDropExp();
            var killer = entity.getKiller();

            if (drops != null)
                event.getDrops().clear();

            if (exp >= 0 || killer != null) {
                if (killer != null)
                    killer.giveExp(event.getDroppedExp());
                event.setDroppedExp(0);
            }
        });
    }


    public LivingEntity getKiller(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity killer)
            return killer;

        if ((event.getDamager() instanceof Projectile projectile)
                && projectile.getShooter() instanceof LivingEntity killer)
            return killer;
        return null;
    }

}
