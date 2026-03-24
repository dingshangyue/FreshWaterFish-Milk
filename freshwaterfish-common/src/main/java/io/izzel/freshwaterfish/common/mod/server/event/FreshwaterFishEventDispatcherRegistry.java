package io.izzel.freshwaterfish.common.mod.server.event;

import io.izzel.freshwaterfish.common.mod.FreshwaterFishMod;
import net.minecraftforge.common.MinecraftForge;

public abstract class FreshwaterFishEventDispatcherRegistry {

    public static void registerAllEventDispatchers() {
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new BlockPlaceEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new EntityPotionEffectEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new EntityEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new EntityTeleportEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new ItemEntityEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new WorldEventDispatcher());
        FreshwaterFishMod.LOGGER.info("registry.forge-event");
    }

}
