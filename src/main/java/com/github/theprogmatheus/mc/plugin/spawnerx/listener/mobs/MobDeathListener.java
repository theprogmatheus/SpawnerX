package com.github.theprogmatheus.mc.plugin.spawnerx.listener.mobs;

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
        var optional = MobEntity.fromEntity(entity);

        if (optional.isPresent()) {
            var mob = optional.get();
            mob.unlink();
        } else {
            optional = MobEntity.fromFakeEntity(entity);
            if (optional.isEmpty())
                return;
            var mob = optional.get();

            // processar os drops, xp etc...

        }
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
