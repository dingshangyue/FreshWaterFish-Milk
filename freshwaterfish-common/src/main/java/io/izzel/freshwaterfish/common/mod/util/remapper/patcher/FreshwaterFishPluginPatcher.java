package io.izzel.freshwaterfish.common.mod.util.remapper.patcher;

import io.izzel.arclight.api.PluginPatcher;
import io.izzel.freshwaterfish.common.mod.FreshwaterFishMod;
import io.izzel.freshwaterfish.common.mod.util.remapper.FreshwaterFishRemapConfig;
import io.izzel.freshwaterfish.common.mod.util.remapper.ClassLoaderRemapper;
import io.izzel.freshwaterfish.common.mod.util.remapper.GlobalClassRepo;
import io.izzel.freshwaterfish.common.mod.util.remapper.PluginTransformer;
import io.izzel.freshwaterfish.common.mod.util.remapper.patcher.integrated.IntegratedPatcher;
import org.bukkit.configuration.file.YamlConfiguration;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FreshwaterFishPluginPatcher implements PluginTransformer {

    private final List<PluginPatcher> list;

    public FreshwaterFishPluginPatcher(List<PluginPatcher> list) {
        this.list = list;
    }

    public static List<PluginPatcher> load(List<PluginTransformer> transformerList) {
        var list = new ArrayList<PluginPatcher>();
        File pluginFolder = new File("plugins");
        if (pluginFolder.exists()) {
            FreshwaterFishMod.LOGGER.info("patcher.loading");
            File[] files = pluginFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".jar")) {
                        loadFromJar(file).ifPresent(list::add);
                    }
                }
                if (!list.isEmpty()) {
                    FreshwaterFishMod.LOGGER.info("patcher.loaded", list.size());
                }
            }
        }
        list.add(new IntegratedPatcher());
        list.sort(Comparator.comparing(PluginPatcher::priority));
        transformerList.add(new FreshwaterFishPluginPatcher(list));
        return list;
    }

    private static Optional<PluginPatcher> loadFromJar(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            JarEntry jarEntry = jarFile.getJarEntry("plugin.yml");
            if (jarEntry != null) {
                try (InputStream stream = jarFile.getInputStream(jarEntry)) {
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
                    String name = configuration.getString("freshwaterfish.patcher");
                    if (name != null) {
                        URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, FreshwaterFishPluginPatcher.class.getClassLoader());
                        Class<?> clazz = Class.forName(name, false, loader);
                        PluginPatcher patcher = clazz.asSubclass(PluginPatcher.class).getConstructor().newInstance();
                        return Optional.of(patcher);
                    }
                }
            }
        } catch (Throwable e) {
            FreshwaterFishMod.LOGGER.debug("patcher.load-error", e);
        }
        return Optional.empty();
    }

    @Override
    public void handleClass(ClassNode node, ClassLoaderRemapper remapper, FreshwaterFishRemapConfig config) {
        for (PluginPatcher patcher : list) {
            patcher.handleClass(node, GlobalClassRepo.INSTANCE);
        }
    }
}
