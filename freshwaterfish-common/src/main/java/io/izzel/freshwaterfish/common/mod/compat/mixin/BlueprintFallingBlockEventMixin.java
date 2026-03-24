package io.izzel.freshwaterfish.common.mod.compat.mixin;

import io.izzel.freshwaterfish.common.mod.compat.ModIds;
import io.izzel.freshwaterfish.common.mod.mixins.annotation.LoadIfMod;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@LoadIfMod(modid = {ModIds.BLUEPRINT}, condition = LoadIfMod.ModCondition.PRESENT)
@Pseudo
@Mixin(targets = "com.teamabnormals.blueprint.core.events.FallingBlockEvent", remap = false)
public class BlueprintFallingBlockEventMixin {

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(
            method = "onBlockFall",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/eventbus/api/IEventBus;post(Lnet/minecraftforge/eventbus/api/Event;)Z"
            )
    )
    private static boolean freshwaterfish$guardOnBlockFall(IEventBus bus, Event event) {
        return freshwaterfish$safePost(bus, event);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(
            method = "onFallingBlockTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/eventbus/api/IEventBus;post(Lnet/minecraftforge/eventbus/api/Event;)Z"
            )
    )
    private static boolean freshwaterfish$guardOnFallingBlockTick(IEventBus bus, Event event) {
        return freshwaterfish$safePost(bus, event);
    }

    private static boolean freshwaterfish$safePost(IEventBus bus, Event event) {
        try {
            return bus.post(event);
        } catch (RuntimeException e) {
            if (freshwaterfish$isMissingNoArgCtor(e)) {
                return false;
            }
            throw e;
        }
    }

    private static boolean freshwaterfish$isMissingNoArgCtor(Throwable error) {
        for (Throwable cause = error; cause != null; cause = cause.getCause()) {
            if (cause instanceof NoSuchMethodException) {
                return true;
            }
        }
        String message = error.getMessage();
        return message != null && message.contains("Error computing listener list");
    }
}
