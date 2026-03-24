package io.izzel.freshwaterfish.common.mixin.optimization.general;

import io.izzel.freshwaterfish.i18n.FreshwaterFishConfig;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Mob.class)
public class MobMixin_Optimization {

    @ModifyConstant(method = "serverAiStep", constant = @Constant(intValue = 2))
    private int freshwaterfish$goalUpdateInterval(int orig) {
        return FreshwaterFishConfig.spec().getOptimization().getGoalSelectorInterval();
    }
}
