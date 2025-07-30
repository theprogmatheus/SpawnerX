package com.github.theprogmatheus.mc.plugin.spawnerx.listener.mobs;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.MobEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.function.Predicate;

public class MobEntityBehaviorController implements Listener {

    private final @NotNull Predicate<Entity> isControlledMob;

    private final boolean allowMobDamage;

    public MobEntityBehaviorController() {
        this.isControlledMob = entity ->
                (entity instanceof LivingEntity living) && MobEntity.fromEntity(living).isPresent();
        this.allowMobDamage = false;
    }


    @EventHandler
    public void onEntityBreed(EntityBreedEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTransform(EntityTransformEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getIgnitingEntity() != null && isControlledMob.test(event.getIgnitingEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (isControlledMob.test(event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleCollide(VehicleEntityCollisionEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (isControlledMob.test(event.getEntity()) && event.getDamager() instanceof Player) {
            return;
        }

        if (event.getDamager() != null && isControlledMob.test(event.getDamager())) {
            if (!allowMobDamage) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCombustByEntity(EntityCombustByEntityEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            Entity combuster = event.getCombuster();
            if (!(combuster instanceof Player || combuster.getType().name().contains("PROJECTILE"))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            if (!allowMobDamage) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEndermanTeleport(EntityTeleportEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDropItem(EntityDropItemEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityMount(EntityMountEvent event) {
        if (isControlledMob.test(event.getEntity()) || isControlledMob.test(event.getMount())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Entity entity))
            return;
        if (isControlledMob.test(entity)) {
            if (!allowMobDamage) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Entity shooter &&
                isControlledMob.test(shooter) && !allowMobDamage) {
            event.setCancelled(true);
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (isControlledMob.test(event.getEntity()) && !allowMobDamage) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCombustByBlock(EntityCombustByBlockEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageGeneric(EntityDamageEvent event) {
        if (isControlledMob.test(event.getEntity()) && !(event instanceof EntityDamageByEntityEvent)) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                event.setCancelled(true);
            }
        }
    }
}
