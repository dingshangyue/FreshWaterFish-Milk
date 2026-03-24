package io.izzel.freshwaterfish.common.mixin.bukkit;

import io.izzel.freshwaterfish.common.bridge.core.item.crafting.IngredientBridge;
import io.izzel.freshwaterfish.common.mod.inventory.FreshwaterFishSpecialIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import org.bukkit.craftbukkit.v.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = CraftRecipe.class, remap = false)
public interface CraftRecipeMixin {

    /**
     * @author IzzelAliz
     * @reason
     */
    @Overwrite
    static RecipeChoice toBukkit(Ingredient list) {
        list.getItems();
        if (!list.isVanilla()) {
            return new FreshwaterFishSpecialIngredient(list);
        }
        net.minecraft.world.item.ItemStack[] items = list.getItems();
        if (items.length == 0) {
            return null;
        } else {
            if (((IngredientBridge) list).bridge$isExact()) {
                List<ItemStack> choices = new ArrayList<>(items.length);
                for (net.minecraft.world.item.ItemStack i : items) {
                    choices.add(CraftItemStack.asBukkitCopy(i));
                }
                return new RecipeChoice.ExactChoice(choices);
            } else {
                List<org.bukkit.Material> choices = new ArrayList<>(items.length);
                for (net.minecraft.world.item.ItemStack i : items) {
                    choices.add(CraftMagicNumbers.getMaterial(i.getItem()));
                }
                return new RecipeChoice.MaterialChoice(choices);
            }
        }
    }

    /**
     * @author IzzelAliz
     * @reason
     */
    @Overwrite
    default Ingredient toNMS(RecipeChoice bukkit, boolean requireNotEmpty) {
        Ingredient stack;
        if (bukkit == null) {
            stack = Ingredient.EMPTY;
        } else if (bukkit instanceof RecipeChoice.MaterialChoice) {
            stack = new Ingredient(((RecipeChoice.MaterialChoice) bukkit).getChoices().stream().map((mat) -> {
                return new Ingredient.ItemValue(CraftItemStack.asNMSCopy(new ItemStack(mat)));
            }));
        } else if (bukkit instanceof RecipeChoice.ExactChoice) {
            stack = new Ingredient(((RecipeChoice.ExactChoice) bukkit).getChoices().stream().map((mat) -> {
                return new Ingredient.ItemValue(CraftItemStack.asNMSCopy(mat));
            }));
            ((IngredientBridge) stack).bridge$setExact(true);
        } else if (bukkit instanceof FreshwaterFishSpecialIngredient) {
            stack = ((FreshwaterFishSpecialIngredient) bukkit).getIngredient();
        } else {
            throw new IllegalArgumentException("Unknown recipe stack instance " + bukkit);
        }

        stack.getItems();
        if (stack.isVanilla() && requireNotEmpty && stack.getItems().length == 0) {
            throw new IllegalArgumentException("Recipe requires at least one non-air choice!");
        } else {
            return stack;
        }
    }
}
