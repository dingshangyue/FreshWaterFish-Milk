package io.izzel.freshwaterfish.common.mixin.core.server;

import io.izzel.freshwaterfish.common.mod.util.FreshwaterFishCaptures;
import net.minecraft.server.WorldLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WorldLoader.class)
public class WorldLoaderMixin {

    @ModifyArg(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/WorldLoader$WorldDataSupplier;get(Lnet/minecraft/server/WorldLoader$DataLoadContext;)Lnet/minecraft/server/WorldLoader$DataLoadOutput;"))
    private static WorldLoader.DataLoadContext freshwaterfish$captureContext(WorldLoader.DataLoadContext context) {
        FreshwaterFishCaptures.captureDataLoadContext(context);
        return context;
    }
}
