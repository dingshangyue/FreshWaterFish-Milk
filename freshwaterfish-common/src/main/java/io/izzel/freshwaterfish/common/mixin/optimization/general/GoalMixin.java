package io.izzel.freshwaterfish.common.mixin.optimization.general;

import io.izzel.freshwaterfish.i18n.FreshwaterFishConfig;
import net.minecraft.world.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Goal.class)
public class GoalMixin {

    @ModifyConstant(method = "reducedTickDelay", constant = @Constant(intValue = 2))
    private static int freshwaterfish$goalUpdateInterval(int orig) {
        return FreshwaterFishConfig.spec().getOptimization().getGoalSelectorInterval();
    }
}
