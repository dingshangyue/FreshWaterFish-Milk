package io.izzel.freshwaterfish.common.mixin.forge;

import io.izzel.freshwaterfish.common.mod.FreshwaterFishMod;
import io.izzel.freshwaterfish.common.mod.server.FreshwaterFishPermissionHandler;
import io.izzel.freshwaterfish.i18n.FreshwaterFishConfig;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.handler.IPermissionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PermissionAPI.class, remap = false)
public class PermissionAPIMixin {

    @Shadow
    private static IPermissionHandler activeHandler;

    @Inject(method = "initializePermissionAPI", at = @At("RETURN"))
    private static void freshwaterfish$init(CallbackInfo ci) {
        if (!FreshwaterFishConfig.spec().getCompat().isForwardPermission()) {
            return;
        }
        var handler = new FreshwaterFishPermissionHandler(activeHandler);
        FreshwaterFishMod.LOGGER.info("permission.forge-to-bukkit", activeHandler.getIdentifier());
        activeHandler = handler;
    }
}
