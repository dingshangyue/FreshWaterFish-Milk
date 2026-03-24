package io.izzel.freshwaterfish.common.mod.server.event;

import io.izzel.freshwaterfish.common.bridge.core.entity.LivingEntityBridge;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class FreshwaterFishEventFactory {

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public static EntityRegainHealthEvent callEntityRegainHealthEvent(Entity entity, float amount, EntityRegainHealthEvent.RegainReason regainReason) {
        EntityRegainHealthEvent event = new EntityRegainHealthEvent(entity, amount, regainReason);
        callEvent(event);
        return event;
    }

    public static EntityResurrectEvent callEntityResurrectEvent(org.bukkit.entity.LivingEntity livingEntity) {
        EntityResurrectEvent event = new EntityResurrectEvent(livingEntity);
        callEvent(event);
        return event;
    }

    public static void callEntityDeathEvent(LivingEntity entity, List<ItemStack> drops) {
        try {
            if (entity instanceof LivingEntityBridge bridge) {
                CraftLivingEntity craftLivingEntity = bridge.bridge$getBukkitEntity();
                EntityDeathEvent event = new EntityDeathEvent(craftLivingEntity, drops, bridge.bridge$getExpReward());
                callEvent(event);
                bridge.bridge$setExpToDrop(event.getDroppedExp());
            } else {
                // LivingEntityBridge mixin not applied to this entity, skip event
                // This may cause plugin compatibility issues but prevents crash
                System.err.println("[FreshwaterFish] WARNING: LivingEntityBridge not implemented for " + entity.getClass().getName() + ", death event skipped");
            }
        } catch (ClassCastException e) {
            // Catch class cast exception to prevent crash
            System.err.println("[FreshwaterFish] ERROR: Class cast exception when handling entity death event for " + entity.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static EntityDeathEvent callEntityDeathEvent(org.bukkit.entity.LivingEntity entity, List<ItemStack> drops, int droppedExp) {
        EntityDeathEvent event = new EntityDeathEvent(entity, drops, droppedExp);
        callEvent(event);
        return event;
    }

    public static EntityDropItemEvent callEntityDropItemEvent(org.bukkit.entity.Entity entity, org.bukkit.entity.Item drop) {
        EntityDropItemEvent bukkitEvent = new EntityDropItemEvent(entity, drop);
        callEvent(bukkitEvent);
        return bukkitEvent;
    }


}
