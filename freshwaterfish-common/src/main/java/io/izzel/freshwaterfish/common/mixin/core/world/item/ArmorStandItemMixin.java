package io.izzel.freshwaterfish.common.mixin.core.world.item;

import io.izzel.freshwaterfish.common.mod.util.DistValidate;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorStandItem;
import net.minecraft.world.item.context.UseOnContext;
import org.bukkit.craftbukkit.v.event.CraftEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandItem.class)
public class ArmorStandItemMixin {

    private transient ArmorStand freshwaterfish$entity;

    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ArmorStand;moveTo(DDDFF)V"))
    public void freshwaterfish$captureEntity(ArmorStand armorStandEntity, double x, double y, double z, float yaw, float pitch) {
        armorStandEntity.moveTo(x, y, z, yaw, pitch);
        freshwaterfish$entity = armorStandEntity;
    }

    @Inject(method = "useOn", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"))
    public void freshwaterfish$entityPlace(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (DistValidate.isValid(context) && CraftEventFactory.callEntityPlaceEvent(context, freshwaterfish$entity).isCancelled()) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
        freshwaterfish$entity = null;
    }
}
