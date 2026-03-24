package io.izzel.freshwaterfish.common.mixin.core.world.level.border;

import io.izzel.freshwaterfish.common.bridge.core.world.border.WorldBorderBridge;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldBorder.class)
public class WorldBorderMixin implements WorldBorderBridge {

    public Level world;

    @Override
    public Level bridge$getWorld() {
        return this.world;
    }

    @Override
    public void bridge$setWorld(Level world) {
        this.world = world;
    }
}
