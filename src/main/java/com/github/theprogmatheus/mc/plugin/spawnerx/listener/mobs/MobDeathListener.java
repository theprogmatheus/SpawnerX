package com.github.theprogmatheus.mc.plugin.spawnerx.listener.mobs;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.MobEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobDeathListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onMobDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled())
            return;

        if ((event.getEntity() instanceof LivingEntity entity)
                && (event.getDamage() >= entity.getHealth()))
            event.setCancelled(MobEntity.handleMobDeath(getKiller(event), entity));
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
