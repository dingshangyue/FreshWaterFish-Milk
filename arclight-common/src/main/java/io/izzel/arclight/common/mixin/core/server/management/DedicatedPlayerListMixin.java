package io.izzel.arclight.common.mixin.core.server.management;

import io.izzel.arclight.common.bridge.bukkit.CraftServerBridge;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import org.bukkit.Bukkit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * Fix adventure-platform-fabric replacing List<ServerPlayer> causing
 * Bukkit#getOnlinePlayers returns nothing
 */
@Mixin(DedicatedPlayerList.class)
public class DedicatedPlayerListMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/server/players/PlayerList;<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/core/LayeredRegistryAccess;Lnet/minecraft/world/level/storage/PlayerDataStorage;I)V"))
    private void arclight$afterSuper(CallbackInfo ci) {
        ((CraftServerBridge)Bukkit.getServer()).bridge$setPlayerList((DedicatedPlayerList)(Object) this);
    }
}
