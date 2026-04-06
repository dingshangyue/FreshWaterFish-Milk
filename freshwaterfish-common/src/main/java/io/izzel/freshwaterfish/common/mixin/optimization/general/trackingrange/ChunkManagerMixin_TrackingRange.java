package io.izzel.freshwaterfish.common.mixin.optimization.general.trackingrange;

import io.izzel.freshwaterfish.i18n.FreshwaterFishConfig;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.entity.Entity;
import org.spigotmc.TrackingRange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChunkMap.class)
public class ChunkManagerMixin_TrackingRange {

    @Unique
    private static final boolean freshwaterfish$applyInactive = FreshwaterFishConfig.spec().getOptimization().useActivationAndTrackingRange();

    @ModifyVariable(method = "addEntity", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;updateInterval()I"))
    private int trackingRange$updateRange(int defaultRange, Entity entity) {
        return freshwaterfish$applyInactive ? TrackingRange.getEntityTrackingRange(entity, defaultRange) : defaultRange;
    }
}
