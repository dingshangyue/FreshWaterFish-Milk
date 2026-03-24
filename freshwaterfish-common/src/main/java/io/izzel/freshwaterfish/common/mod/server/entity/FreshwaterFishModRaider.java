package io.izzel.freshwaterfish.common.mod.server.entity;

import net.minecraft.world.entity.raid.Raider;
import org.bukkit.craftbukkit.v.CraftServer;
import org.bukkit.craftbukkit.v.entity.CraftRaider;

public class FreshwaterFishModRaider extends CraftRaider {

    public FreshwaterFishModRaider(CraftServer server, Raider entity) {
        super(server, entity);
    }
}
