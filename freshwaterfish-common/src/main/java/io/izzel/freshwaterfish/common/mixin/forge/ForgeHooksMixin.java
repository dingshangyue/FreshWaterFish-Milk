package io.izzel.freshwaterfish.common.mixin.forge;

import io.izzel.freshwaterfish.common.mod.util.FreshwaterFishCaptures;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin {

    @Inject(method = "onPlaceItemIntoWorld", remap = false, at = @At("HEAD"))
    private static void freshwaterfish$captureHand(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        FreshwaterFishCaptures.capturePlaceEventHand(context.getHand());
    }

    @Inject(method = "onPlaceItemIntoWorld", remap = false, at = @At("RETURN"))
    private static void freshwaterfish$removeHand(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        FreshwaterFishCaptures.getPlaceEventHand(InteractionHand.MAIN_HAND);
    }
}
