package io.izzel.freshwaterfish.common.mixin.core.world.level.gameevent;

import io.izzel.freshwaterfish.common.bridge.core.entity.EntityBridge;
import io.izzel.freshwaterfish.common.bridge.core.world.WorldBridge;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventDispatcher;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v.CraftGameEvent;
import org.bukkit.event.world.GenericGameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameEventDispatcher.class)
public class GameEventDispatcherMixin {

    @Shadow
    @Final
    private ServerLevel level;

    private transient int freshwaterfish$newRadius;

    @Inject(method = "post", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$gameEvent(GameEvent gameEvent, Vec3 vec3, GameEvent.Context context, CallbackInfo ci) {
        var entity = context.sourceEntity();
        var i = gameEvent.getNotificationRadius();
        GenericGameEvent event = new GenericGameEvent(CraftGameEvent.minecraftToBukkit(gameEvent),
                new Location(((WorldBridge) this.level).bridge$getWorld(), vec3.x(), vec3.y(), vec3.z()), (entity == null) ? null : ((EntityBridge) entity).bridge$getBukkitEntity(), i, !Bukkit.isPrimaryThread());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            ci.cancel();
        } else {
            freshwaterfish$newRadius = event.getRadius();
        }
    }

    @Redirect(method = "post", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/gameevent/GameEvent;getNotificationRadius()I"))
    private int freshwaterfish$applyRadius(GameEvent instance) {
        return freshwaterfish$newRadius;
    }
}
