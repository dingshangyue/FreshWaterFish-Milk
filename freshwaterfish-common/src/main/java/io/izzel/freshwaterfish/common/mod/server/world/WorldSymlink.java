package io.izzel.freshwaterfish.common.mod.server.world;

import io.izzel.freshwaterfish.common.mod.FreshwaterFishMod;
import net.minecraft.world.level.storage.DerivedLevelData;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorldSymlink {

    public static void create(DerivedLevelData worldInfo, File dimensionFolder) {
        String name = worldInfo.getLevelName();
        Path source = new File(Bukkit.getWorldContainer(), name).toPath();
        Path dest = dimensionFolder.toPath();
        try {
            if (!Files.isSymbolicLink(source)) {
                if (Files.exists(source)) {
                    FreshwaterFishMod.LOGGER.warn("symlink-file-exist", source);
                    return;
                }
                Files.createSymbolicLink(source, dest);
            }
        } catch (UnsupportedOperationException e) {
            FreshwaterFishMod.LOGGER.warn("error-symlink", e);
        } catch (IOException e) {
            FreshwaterFishMod.LOGGER.error("symlink.create-error", e);
        }
    }
}
