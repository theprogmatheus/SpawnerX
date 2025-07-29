package com.github.theprogmatheus.mc.plugin.spawnerx.listener.mobs;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.MobConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.MobEntity;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MobDeathListener implements Listener {

    private final Plugin plugin;

    @EventHandler(priority = EventPriority.MONITOR)
    void onMobDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled())
            return;

        if (event.getEntity() instanceof LivingEntity entity) {
            MobEntity.fromEntity(entity).ifPresent(mob -> {
                var config = mob.getConfig();

                if (config.isHitKill())
                    event.setDamage(entity.getHealth());

                if (config.isAi() && !config.isKnockBack())
                    Bukkit.getScheduler().runTaskLater(this.plugin, () -> entity.setVelocity(new Vector(0, 0, 0)), 1);

                if ((event.getDamage() >= entity.getHealth())) {
                    mob.simulateDeath(getKiller(event));

                    event.setCancelled(mob.getStackedAmount() > 0);
                }
            });

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
