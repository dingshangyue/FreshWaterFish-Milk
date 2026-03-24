package io.izzel.freshwaterfish.boot.application;

import io.izzel.arclight.api.EnumHelper;
import io.izzel.arclight.api.Unsafe;
import io.izzel.freshwaterfish.boot.AbstractBootstrap;
import io.izzel.freshwaterfish.i18n.FreshwaterFishConfig;
import io.izzel.freshwaterfish.i18n.FreshwaterFishLocale;

import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.function.Consumer;

public class ApplicationBootstrap extends AbstractBootstrap implements Consumer<String[]> {


    private static final int MIN_DEPRECATED_VERSION = 60;
    private static final int MIN_DEPRECATED_JAVA_VERSION = 16;

    @Override
    @SuppressWarnings("unchecked")
    public void accept(String[] args) {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        System.setProperty("log4j.jul.LoggerAdapter", "io.izzel.freshwaterfish.boot.log.FreshwaterFishLoggerAdapter");
        System.setProperty("log4j.configurationFile", "freshwaterfish-log4j2.xml");
        FreshwaterFishLocale.info("i18n.using-language", FreshwaterFishConfig.spec().getLocale().getCurrent(), FreshwaterFishConfig.spec().getLocale().getFallback());
        try {
            int javaVersion = (int) Float.parseFloat(System.getProperty("java.class.version"));
            if (javaVersion < MIN_DEPRECATED_VERSION) {
                FreshwaterFishLocale.error("java.deprecated", System.getProperty("java.version"), MIN_DEPRECATED_JAVA_VERSION);
                Thread.sleep(3000);
            }
            Unsafe.ensureClassInitialized(EnumHelper.class);
        } catch (Throwable t) {
            System.err.println("Your Java is not compatible with FreshwaterFish.");
            t.printStackTrace();
            return;
        }
        try {
            this.setupMod();
            this.dirtyHacks();
            int targetIndex = Arrays.asList(args).indexOf("--launchTarget");
            if (targetIndex >= 0 && targetIndex < args.length - 1) {
                args[targetIndex + 1] = "freshwaterfishserver";
            }
            ServiceLoader.load(getClass().getModule().getLayer(), Consumer.class).stream()
                    .filter(it -> !it.type().getName().contains("freshwaterfish"))
                    .findFirst().orElseThrow().get().accept(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Fail to launch FreshwaterFish.");
        }
    }
}
