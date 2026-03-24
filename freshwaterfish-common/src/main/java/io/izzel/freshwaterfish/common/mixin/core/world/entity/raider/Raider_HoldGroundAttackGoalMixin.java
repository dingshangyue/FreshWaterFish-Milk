package io.izzel.freshwaterfish.common.mixin.core.world.entity.raider;

import io.izzel.freshwaterfish.common.bridge.core.entity.MobEntityBridge;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.raid.Raider;
import org.bukkit.event.entity.EntityTargetEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Raider.HoldGroundAttackGoal.class)
public abstract class Raider_HoldGroundAttackGoalMixin {

    @Redirect(method = "start", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/raid/Raider;setTarget(Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void freshwaterfish$reason(Raider abstractRaiderEntity, LivingEntity entitylivingbaseIn) {
        ((MobEntityBridge) abstractRaiderEntity).bridge$pushGoalTargetReason(EntityTargetEvent.TargetReason.FOLLOW_LEADER, true);
        abstractRaiderEntity.setTarget(entitylivingbaseIn);
    }

    @Redirect(method = "stop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/raid/Raider;setTarget(Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void freshwaterfish$reason2(Raider abstractRaiderEntity, LivingEntity entitylivingbaseIn) {
        ((MobEntityBridge) abstractRaiderEntity).bridge$pushGoalTargetReason(EntityTargetEvent.TargetReason.FOLLOW_LEADER, true);
        abstractRaiderEntity.setTarget(entitylivingbaseIn);
    }
}
