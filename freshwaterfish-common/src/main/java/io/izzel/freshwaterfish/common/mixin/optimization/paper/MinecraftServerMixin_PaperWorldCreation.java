package io.izzel.freshwaterfish.common.mixin.optimization.paper;

import io.izzel.freshwaterfish.common.bridge.optimization.paper.WorldCreationBridge;
import io.izzel.freshwaterfish.common.mod.compat.ModIds;
import io.izzel.freshwaterfish.common.mod.mixins.annotation.LoadIfMod;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@LoadIfMod(modid = {ModIds.MODERNFIX}, condition = LoadIfMod.ModCondition.ABSENT)
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin_PaperWorldCreation implements WorldCreationBridge {

    private boolean freshwaterfish$isIteratingOverLevels = false;

    @Inject(method = "tickChildren", at = @At("HEAD"))
    private void freshwaterfish$setIteratingFlag(CallbackInfo ci) {
        this.freshwaterfish$isIteratingOverLevels = true;
    }

    @Inject(method = "tickChildren", at = @At("RETURN"))
    private void freshwaterfish$unsetIteratingFlag(CallbackInfo ci) {
        this.freshwaterfish$isIteratingOverLevels = false;
    }

    @Override
    public boolean freshwaterfish$isIteratingOverLevels() {
        return this.freshwaterfish$isIteratingOverLevels;
    }
}
