package io.izzel.freshwaterfish.i18n;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ValueType;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.Callable;

public record FreshwaterFishLocale(String current, String fallback, ConfigurationNode node) {

    private static FreshwaterFishLocale instance;

    static {
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void info(String path, Object... args) {
        System.out.println(instance.format(path, args));
    }

    public static void error(String path, Object... args) {
        System.err.println(instance.format(path, args));
    }

    public static FreshwaterFishLocale getInstance() {
        return instance;
    }

    private static void init() throws Exception {
        Logger logger = LogManager.getLogger("FreshwaterFishLocale-Debug");
        logger.debug("FreshwaterFishLocale initialization starting");
        Map.Entry<String, String> entry = getLocale();
        String current = entry.getKey();
        String fallback = entry.getValue();
        logger.debug("Current locale: {}, Fallback: {}", current, fallback);
        InputStream stream = FreshwaterFishLocale.class.getResourceAsStream("/META-INF/i18n/" + fallback + ".yml");
        if (stream == null) throw new RuntimeException("Fallback locale is not found: " + fallback);
        logger.debug("Loading fallback locale resource: {}", fallback);
        ConfigurationNode node = YAMLConfigurationLoader.builder().setSource(localeSource(fallback)).build().load();
        instance = new FreshwaterFishLocale(current, fallback, node);
        logger.debug("FreshwaterFishLocale instance created with fallback");
        if (!current.equals(fallback)) {
            try {
                logger.debug("Loading current locale resource: {}", current);
                ConfigurationNode curNode = YAMLConfigurationLoader.builder().setSource(localeSource(current)).build().load();
                curNode.mergeValuesFrom(node);
                instance = new FreshwaterFishLocale(current, fallback, curNode);
                logger.debug("FreshwaterFishLocale instance updated with current locale");
            } catch (Exception e) {
                logger.debug("Failed to load current locale: {}", e.getMessage());
                System.err.println(instance.format("i18n.current-not-available", current));
            }
        }
        logger.debug("FreshwaterFishLocale initialization completed");
    }

    private static Callable<BufferedReader> localeSource(String path) {
        return () -> new BufferedReader(new InputStreamReader(FreshwaterFishLocale.class.getResourceAsStream("/META-INF/i18n/" + path + ".yml"), StandardCharsets.UTF_8));
    }

    private static Map.Entry<String, String> getLocale() {
        Logger logger = LogManager.getLogger("FreshwaterFishLocale-Debug");
        try {
            Path path = Paths.get("freshwaterfish.yml");
            logger.debug("Looking for config file at: {}", path.toAbsolutePath());
            if (!Files.exists(path)) {
                logger.debug("Config file freshwaterfish.yml not found, using system locale");
                throw new Exception();
            } else {
                logger.debug("Config file found, reading locale settings");
                ConfigurationNode node = YAMLConfigurationLoader.builder().setPath(path).build().load();
                ConfigurationNode locale = node.getNode("locale");
                String current = locale.getNode("current").getString(currentLocale());
                String fallback = locale.getNode("fallback").getString("zh_cn");
                logger.debug("Configured locale - Current: {}, Fallback: {}", current, fallback);
                return new AbstractMap.SimpleImmutableEntry<>(current, fallback);
            }
        } catch (Throwable t) {
            String systemLocale = currentLocale();
            logger.debug("Using system locale: {} with zh_cn fallback", systemLocale);
            return new AbstractMap.SimpleImmutableEntry<>(systemLocale, "zh_cn");
        }
    }

    private static String currentLocale() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage().toLowerCase(Locale.ROOT) + "_" + locale.getCountry().toLowerCase(Locale.ROOT);
    }

    public String format(String node, Object... args) {
        return MessageFormat.format(get(node), args);
    }

    public String get(String path) {
        Logger logger = LogManager.getLogger("FreshwaterFishLocale-Debug");
        if (path.contains("command") || path.contains("i18n") || path.contains("error")) {
            logger.debug("Getting localized string for: {}", path);
        }
        String result = getOption(path).orElse(path);
        if ((path.contains("command") || path.contains("i18n") || path.contains("error")) && result.equals(path)) {
            logger.debug("No translation found for: {}, returning original", path);
        }
        return result;
    }

    public Optional<String> getOption(String path) {
        ConfigurationNode node = this.node.getNode((Object[]) path.split("\\."));
        if (node.getValueType() == ValueType.LIST) {
            StringJoiner joiner = new StringJoiner("\n");
            for (ConfigurationNode configurationNode : node.getChildrenList()) {
                joiner.add(configurationNode.getString());
            }
            return Optional.ofNullable(joiner.toString());
        } else {
            return Optional.ofNullable(node.getString());
        }
    }
}
