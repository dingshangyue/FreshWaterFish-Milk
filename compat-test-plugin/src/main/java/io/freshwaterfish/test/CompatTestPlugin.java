package io.freshwaterfish.test;

import org.bukkit.plugin.java.JavaPlugin;

public class CompatTestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("CompatTestPlugin enabled.");
    }
}
