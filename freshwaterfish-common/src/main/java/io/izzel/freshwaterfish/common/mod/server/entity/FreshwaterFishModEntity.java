package io.izzel.freshwaterfish.common.mod.server.entity;

import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v.CraftServer;
import org.bukkit.craftbukkit.v.entity.CraftEntity;

public class FreshwaterFishModEntity extends CraftEntity {

    public FreshwaterFishModEntity(CraftServer server, Entity entity) {
        super(server, entity);
    }
}
