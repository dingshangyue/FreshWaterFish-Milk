package io.izzel.freshwaterfish.boot.application;

import net.minecraftforge.fml.loading.targets.ForgeServerLaunchHandler;

public class FreshwaterFishLaunchHandler extends ForgeServerLaunchHandler {

    @Override
    public String name() {
        return "freshwaterfishserver";
    }

    @Override
    protected String[] preLaunch(String[] arguments, ModuleLayer layer) {
        // skip the log4j configuration reloading
        return arguments;
    }
}
