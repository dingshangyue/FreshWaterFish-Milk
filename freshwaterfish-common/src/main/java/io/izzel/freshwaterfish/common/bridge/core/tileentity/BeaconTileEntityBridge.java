package io.izzel.freshwaterfish.common.bridge.core.tileentity;

import org.bukkit.potion.PotionEffect;

public interface BeaconTileEntityBridge {

    PotionEffect bridge$getPrimaryEffect();

    PotionEffect bridge$getSecondaryEffect();
}
