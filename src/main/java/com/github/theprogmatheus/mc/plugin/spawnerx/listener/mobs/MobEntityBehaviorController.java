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
import org.bukkit.event.world.StructureGrowEvent;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.function.Predicate;

/**
 * This listener restricts unwanted vanilla behaviors for special MobEntity instances.
 * The goal is to ensure these entities act only as stacked mobs designed for death and loot drop.
 */
public class MobEntityBehaviorController implements Listener {

    /**
     * Predicate to determine if an entity should be treated as a controlled MobEntity.
     */
    private final @NotNull Predicate<Entity> isControlledMob;

    /**
     * Determines whether controlled mobs are allowed to attack or cause damage.
     */
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
        // Cancel if source is a controlled mob
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
        // Allow players to damage controlled mobs
        if (isControlledMob.test(event.getEntity()) && event.getDamager() instanceof Player) {
            return;
        }

        // Prevent mobs from damaging others unless allowed
        if (event.getDamager() != null && isControlledMob.test(event.getDamager())) {
            if (!allowMobDamage) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCombustByEntity(EntityCombustByEntityEvent event) {
        if (isControlledMob.test(event.getEntity())) {
            // Allow fire if the source is natural (lava, fire aspect, etc.)
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
    public void onStructureGrow(StructureGrowEvent event) {
        // Optional: If structure growth is triggered by a mob (rare), you may want to check and cancel.
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
}
