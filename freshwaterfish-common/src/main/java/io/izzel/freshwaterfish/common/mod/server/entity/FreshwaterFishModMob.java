package io.izzel.freshwaterfish.common.mod.server.entity;

import net.minecraft.world.entity.Mob;
import org.bukkit.craftbukkit.v.CraftServer;
import org.bukkit.craftbukkit.v.entity.CraftMob;

public class FreshwaterFishModMob extends CraftMob {

    public FreshwaterFishModMob(CraftServer server, Mob entity) {
        super(server, entity);
    }
}
