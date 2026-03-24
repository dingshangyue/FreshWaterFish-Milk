package io.izzel.freshwaterfish.common.mod.server.entity;

import net.minecraft.world.entity.projectile.Projectile;
import org.bukkit.craftbukkit.v.CraftServer;
import org.bukkit.craftbukkit.v.entity.CraftProjectile;

public class FreshwaterFishModProjectile extends CraftProjectile {

    public FreshwaterFishModProjectile(CraftServer server, Projectile entity) {
        super(server, entity);
    }
}
