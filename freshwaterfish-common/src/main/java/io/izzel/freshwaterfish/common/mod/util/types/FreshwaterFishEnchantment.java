package io.izzel.freshwaterfish.common.mod.util.types;

import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.craftbukkit.v.enchantments.CraftEnchantment;
import org.jetbrains.annotations.NotNull;

public class FreshwaterFishEnchantment extends CraftEnchantment {

    private final String name;

    public FreshwaterFishEnchantment(Enchantment target, String name) {
        super(target);
        this.name = name;
    }

    @Override
    public @NotNull String getName() {
        String name = super.getName();
        if (name.startsWith("UNKNOWN_ENCHANT_")) {
            return this.name;
        } else {
            return name;
        }
    }
}
