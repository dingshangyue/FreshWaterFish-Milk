package io.izzel.freshwaterfish.i18n;

import com.google.common.reflect.TypeToken;
import io.izzel.freshwaterfish.i18n.conf.ConfigSpec;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FreshwaterFishConfig {

    private static FreshwaterFishConfig instance;

    static {
        try {
            load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ConfigurationNode node;
    private final ConfigSpec spec;

    public FreshwaterFishConfig(ConfigurationNode node) throws ObjectMappingException {
        this.node = node;
        this.spec = this.node.getValue(TypeToken.of(ConfigSpec.class));
    }

    public static ConfigSpec spec() {
        return instance.spec;
    }

    private static void load() throws Exception {
        Path path = Paths.get("freshwaterfish.yml");
        boolean fileExisted = Files.exists(path);

        if (!fileExisted) {
            instance = createInitialConfig(path);
        } else {
            instance = loadExistingConfig(path);
        }

        LoggingConfigurator.apply(instance.spec);
    }

    private static FreshwaterFishConfig createInitialConfig(Path path) throws Exception {
        String currentLocale = FreshwaterFishLocale.getInstance().current();
        try (InputStream is = FreshwaterFishConfig.class.getResourceAsStream("/META-INF/freshwaterfish.yml")) {
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String processed = I18nCommentInjector.injectComments(content, currentLocale);
            Files.write(path, processed.getBytes(StandardCharsets.UTF_8));
        }

        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder()
                .setPath(path)
                .setIndent(2)
                .build();
        ConfigurationNode cur = loader.load();
        return new FreshwaterFishConfig(cur);
    }

    private static FreshwaterFishConfig loadExistingConfig(Path path) throws Exception {
        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder()
                .setPath(path)
                .setIndent(2)
                .build();
        ConfigurationNode cur = loader.load();

        cur.getNode("locale", "current").setValue(FreshwaterFishLocale.getInstance().current());
        return new FreshwaterFishConfig(cur);
    }


    public ConfigSpec getSpec() {
        return spec;
    }

    public ConfigurationNode getNode() {
        return node;
    }

    public ConfigurationNode get(String path) {
        return this.node.getNode((Object) path.split("\\."));
    }
}
