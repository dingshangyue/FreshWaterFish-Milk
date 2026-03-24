package io.izzel.freshwaterfish.common.mixin.bukkit;

import io.izzel.freshwaterfish.common.mod.util.log.FreshwaterFishPluginLogger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JavaPlugin.class)
public class JavaPluginMixin {

    @Redirect(method = "init", remap = false, at = @At(value = "NEW", target = "org/bukkit/plugin/PluginLogger"))
    private PluginLogger freshwaterfish$createLogger(Plugin plugin) {
        return new FreshwaterFishPluginLogger(plugin);
    }

}
