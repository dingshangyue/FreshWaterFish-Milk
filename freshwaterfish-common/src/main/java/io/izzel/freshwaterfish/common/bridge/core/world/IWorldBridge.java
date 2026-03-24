package io.izzel.freshwaterfish.common.bridge.core.world;

import net.minecraft.server.level.ServerLevel;

public interface IWorldBridge {

    ServerLevel bridge$getMinecraftWorld();
}
