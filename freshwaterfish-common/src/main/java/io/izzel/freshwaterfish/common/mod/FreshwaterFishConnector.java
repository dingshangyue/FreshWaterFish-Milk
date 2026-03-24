package io.izzel.freshwaterfish.common.mod;

import io.izzel.freshwaterfish.common.mod.util.log.FreshwaterFishI18nLogger;
import io.izzel.arclight.mixin.injector.EjectorInfo;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

public class FreshwaterFishConnector implements IMixinConnector {

    public static final Logger LOGGER = FreshwaterFishI18nLogger.getLogger("FreshwaterFish");

    @Override
    public void connect() {
        InjectionInfo.register(EjectorInfo.class);
        Mixins.addConfiguration("mixins.freshwaterfish.core.json");
        Mixins.addConfiguration("mixins.freshwaterfish.bukkit.json");
        Mixins.addConfiguration("mixins.freshwaterfish.forge.json");
        Mixins.addConfiguration("mixins.freshwaterfish.compat.json");
        LOGGER.info("mixin-load.core");
        Mixins.addConfiguration("mixins.freshwaterfish.impl.forge.optimization.json");
        LOGGER.info("mixin-load.optimization");
        Mixins.addConfiguration("mixins.freshwaterfish.impl.forge.paper.json");
    }
}
