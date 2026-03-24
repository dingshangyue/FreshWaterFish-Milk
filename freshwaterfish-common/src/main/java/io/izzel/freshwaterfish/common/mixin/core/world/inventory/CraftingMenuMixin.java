package io.izzel.freshwaterfish.common.mixin.core.world.inventory;

import io.izzel.freshwaterfish.common.bridge.core.entity.player.PlayerEntityBridge;
import io.izzel.freshwaterfish.common.bridge.core.inventory.CraftingInventoryBridge;
import io.izzel.freshwaterfish.common.bridge.core.inventory.container.ContainerBridge;
import io.izzel.freshwaterfish.common.bridge.core.inventory.container.PosContainerBridge;
import io.izzel.freshwaterfish.common.mod.util.FreshwaterFishCaptures;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v.event.CraftEventFactory;
import org.bukkit.craftbukkit.v.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v.inventory.CraftInventoryView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin extends AbstractContainerMenuMixin implements PosContainerBridge {

    private static transient boolean freshwaterfish$isRepair;
    // @formatter:off
    @Mutable @Shadow @Final private CraftingContainer craftSlots;
    @Shadow @Final private ResultContainer resultSlots;
    // @formatter:on
    private CraftInventoryView bukkitEntity;
    private Inventory playerInventory;

    @Redirect(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", remap = false, target = "Ljava/util/Optional;isPresent()Z"))
    private static boolean freshwaterfish$testRepair(Optional<?> optional) {
        freshwaterfish$isRepair = optional.orElse(null) instanceof RepairItemRecipe;
        return optional.isPresent();
    }

    @ModifyVariable(method = "slotChangedCraftingGrid", ordinal = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private static ItemStack freshwaterfish$preCraft(ItemStack stack, AbstractContainerMenu container, Level level, Player player, CraftingContainer craftingContainer, ResultContainer resultContainer) {
        return CraftEventFactory.callPreCraftEvent(craftingContainer, resultContainer, stack, ((ContainerBridge) container).bridge$getBukkitView(), freshwaterfish$isRepair);
    }

    @Accessor("access")
    public abstract ContainerLevelAccess bridge$getWorldPos();

    @Inject(method = "stillValid", cancellable = true, at = @At("HEAD"))
    public void freshwaterfish$unreachable(Player playerIn, CallbackInfoReturnable<Boolean> cir) {
        if (!bridge$isCheckReachable()) cir.setReturnValue(true);
    }

    @Inject(method = "slotsChanged", at = @At("HEAD"))
    public void freshwaterfish$capture(Container inventoryIn, CallbackInfo ci) {
        FreshwaterFishCaptures.captureWorkbenchContainer((CraftingMenu) (Object) this);
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    public void freshwaterfish$init(int i, Inventory playerInventory, ContainerLevelAccess callable, CallbackInfo ci) {
        ((CraftingInventoryBridge) this.craftSlots).bridge$setOwner(playerInventory.player);
        ((CraftingInventoryBridge) this.craftSlots).bridge$setResultInventory(this.resultSlots);
        this.playerInventory = playerInventory;
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryCrafting inventory = new CraftInventoryCrafting(this.craftSlots, this.resultSlots);
        bukkitEntity = new CraftInventoryView(((PlayerEntityBridge) this.playerInventory.player).bridge$getBukkitEntity(), inventory, (AbstractContainerMenu) (Object) this);
        return bukkitEntity;
    }
}
