package io.izzel.freshwaterfish.common.mixin.optimization.general;

import io.izzel.freshwaterfish.common.mod.util.log.FreshwaterFishI18nLogger;
import io.izzel.freshwaterfish.i18n.FreshwaterFishConfig;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerLevel.class)
public class ServerLevelMixin_Optimize {
    private static final org.apache.logging.log4j.Logger LOGGER = FreshwaterFishI18nLogger.getLogger("ServerLevel-Optimize");

    @ModifyVariable(method = "tickChunk", at = @At("HEAD"), argsOnly = true)
    private int luminara$modifyChunkLoadRate(int chunks) {
        var config = FreshwaterFishConfig.spec().getOptimization().getChunkOptimization();
        if (config.isOptimizeChunkLoading()) {
            int limited = Math.min(chunks, config.getChunkLoadRateLimit());
            if (limited < chunks) {
                LOGGER.debug("optimization.chunk.rate-limit", chunks, limited);
            }
            return limited;
        }
        return chunks;
    }

}
