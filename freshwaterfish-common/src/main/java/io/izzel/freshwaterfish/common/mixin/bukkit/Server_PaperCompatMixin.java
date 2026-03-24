package io.izzel.freshwaterfish.common.mixin.bukkit;

import io.izzel.freshwaterfish.common.adventure.PaperAdventure;
import io.izzel.freshwaterfish.common.mod.FreshwaterFishConstants;
import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Server.class, remap = false)
public interface Server_PaperCompatMixin {

    // Paper API compatibility: Server#getCurrentTick()
    default int getCurrentTick() {
        return FreshwaterFishConstants.currentTick;
    }

    // Paper API compatibility: Server#createInventory(InventoryHolder, int, Component)
    default Inventory createInventory(InventoryHolder owner, int size, net.kyori.adventure.text.Component title) throws IllegalArgumentException {
        return ((Server) this).createInventory(owner, size, PaperAdventure.adventureToLegacy(title));
    }
}
