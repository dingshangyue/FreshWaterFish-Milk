package io.izzel.freshwaterfish.common.mod.server.entity;

import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import org.bukkit.craftbukkit.v.CraftServer;
import org.bukkit.craftbukkit.v.entity.CraftThrowableProjectile;

public class FreshwaterFishModThrowableProjectile extends CraftThrowableProjectile {

    public FreshwaterFishModThrowableProjectile(CraftServer server, ThrowableItemProjectile entity) {
        super(server, entity);
    }
}
