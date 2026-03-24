package io.izzel.freshwaterfish.common.mod.server.entity;

import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import org.bukkit.craftbukkit.v.CraftServer;
import org.bukkit.craftbukkit.v.entity.CraftMinecartContainer;

public class FreshwaterFishModMinecartContainer extends CraftMinecartContainer {

    public FreshwaterFishModMinecartContainer(CraftServer server, AbstractMinecartContainer entity) {
        super(server, entity);
    }
}
