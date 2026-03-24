package io.izzel.freshwaterfish.common.mod.server.entity;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.bukkit.craftbukkit.v.CraftServer;
import org.bukkit.craftbukkit.v.entity.CraftMinecart;

public class FreshwaterFishModMinecart extends CraftMinecart {

    public FreshwaterFishModMinecart(CraftServer server, AbstractMinecart entity) {
        super(server, entity);
    }
}
