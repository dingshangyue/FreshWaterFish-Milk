package io.izzel.freshwaterfish.common.bridge.core.item.crafting;

import net.minecraft.world.item.crafting.Recipe;

public interface RecipeManagerBridge {

    void bridge$addRecipe(Recipe<?> recipe);

    void bridge$clearRecipes();
}
