package io.izzel.freshwaterfish.common.mixin.optimization.general.realtime;

import io.izzel.freshwaterfish.common.mod.FreshwaterFishConstants;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerGameMode.class)
public class PlayerInteractionManagerMixin_Realtime {

    @Shadow
    private int gameTicks;

    private int lastTick = FreshwaterFishConstants.currentTick - 1;

    @Redirect(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/server/level/ServerPlayerGameMode;gameTicks:I"))
    private void freshwaterfish$useWallTime(ServerPlayerGameMode playerInteractionManager, int value) {
        int elapsedTicks = FreshwaterFishConstants.currentTick - this.lastTick;
        if (elapsedTicks < 1) {
            elapsedTicks = 1;
        }
        this.gameTicks += elapsedTicks;
        this.lastTick = FreshwaterFishConstants.currentTick;
    }
}
