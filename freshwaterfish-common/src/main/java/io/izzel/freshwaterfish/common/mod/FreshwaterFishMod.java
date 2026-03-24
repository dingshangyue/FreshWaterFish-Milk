package io.izzel.freshwaterfish.common.mod;

import io.izzel.freshwaterfish.common.mod.server.event.FreshwaterFishEventDispatcherRegistry;
import io.izzel.freshwaterfish.common.mod.util.BungeeComponentPreloader;
import io.izzel.freshwaterfish.common.mod.util.log.FreshwaterFishI18nLogger;
import io.izzel.freshwaterfish.i18n.FreshwaterFishConfig;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;
import java.io.PrintStream;

@Mod("freshwaterfish")
public class FreshwaterFishMod {

    public static final Logger LOGGER = FreshwaterFishI18nLogger.getLogger("FreshwaterFish");

    public FreshwaterFishMod(FMLJavaModLoadingContext context) {
        LOGGER.info("mod-load");
        System.setOut(new LoggingPrintStream("STDOUT", System.out, Level.INFO));
        System.setErr(new LoggingPrintStream("STDERR", System.err, Level.ERROR));

        if (FreshwaterFishConfig.spec().getCompat().isPreloadBungeeChatClasses()) {
            BungeeComponentPreloader.preloadBungeeClasses();
        }

        FreshwaterFishEventDispatcherRegistry.registerAllEventDispatchers();
        context.registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        context.getModEventBus().addListener(this::onCommonSetup);
    }

    public static boolean isModLoaded(String modid) {
        return ModList.get() != null ? ModList.get().isLoaded(modid) : FMLLoader.getLoadingModList().getModFileById(modid) != null;
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        // Common setup complete
    }

    private static class LoggingPrintStream extends PrintStream {

        private final Logger logger;
        private final Level level;

        public LoggingPrintStream(String name, @NotNull OutputStream out, Level level) {
            super(out);
            this.logger = LogManager.getLogger(name);
            this.level = level;
        }

        @Override
        public void println(@Nullable String x) {
            logger.log(level, x);
        }

        @Override
        public void println(@Nullable Object x) {
            logger.log(level, String.valueOf(x));
        }
    }
}
