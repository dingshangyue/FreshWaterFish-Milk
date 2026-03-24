package io.izzel.freshwaterfish.common.bridge.core.world.server;

import io.izzel.freshwaterfish.common.mod.util.FreshwaterFishCallbackExecutor;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.function.BooleanSupplier;

public interface ChunkMapBridge {

    void bridge$tick(BooleanSupplier hasMoreTime);

    Iterable<ChunkHolder> bridge$getLoadedChunksIterable();

    void bridge$tickEntityTracker();

    FreshwaterFishCallbackExecutor bridge$getCallbackExecutor();

    ChunkHolder bridge$chunkHolderAt(long chunkPos);

    void bridge$setViewDistance(int i);

    void bridge$setChunkGenerator(ChunkGenerator generator);
}
