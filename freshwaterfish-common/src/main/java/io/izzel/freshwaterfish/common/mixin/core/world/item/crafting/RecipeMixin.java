package io.izzel.freshwaterfish.common.mixin.core.world.item.crafting;

import io.izzel.freshwaterfish.common.bridge.core.item.crafting.IRecipeBridge;
import io.izzel.freshwaterfish.common.mod.util.FreshwaterFishSpecialRecipe;
import org.bukkit.inventory.Recipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(net.minecraft.world.item.crafting.Recipe.class)
public interface RecipeMixin extends IRecipeBridge {

    default Recipe toBukkitRecipe() {
        return bridge$toBukkitRecipe();
    }

    @Override
    default Recipe bridge$toBukkitRecipe() {
        return new FreshwaterFishSpecialRecipe((net.minecraft.world.item.crafting.Recipe<?>) this);
    }
}
