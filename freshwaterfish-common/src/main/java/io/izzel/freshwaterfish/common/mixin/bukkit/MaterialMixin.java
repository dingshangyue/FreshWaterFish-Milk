package io.izzel.freshwaterfish.common.mixin.bukkit;

import com.google.common.collect.ImmutableMap;
import io.izzel.freshwaterfish.common.bridge.bukkit.MaterialBridge;
import io.izzel.freshwaterfish.common.bridge.core.block.FireBlockBridge;
import io.izzel.freshwaterfish.common.mod.FreshwaterFishMod;
import io.izzel.freshwaterfish.i18n.LocalizedException;
import io.izzel.freshwaterfish.i18n.conf.MaterialPropertySpec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v.block.CraftBlock;
import org.bukkit.craftbukkit.v.block.CraftBlockState;
import org.bukkit.craftbukkit.v.block.CraftBlockStates;
import org.bukkit.craftbukkit.v.inventory.*;
import org.bukkit.craftbukkit.v.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v.util.CraftNamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(value = Material.class, remap = false)
public abstract class MaterialMixin implements MaterialBridge {

    private static final Map<String, BiFunction<Material, CraftMetaItem, ItemMeta>> TYPES = ImmutableMap
            .<String, BiFunction<Material, CraftMetaItem, ItemMeta>>builder()
            .put("ARMOR_STAND", (a, b) -> b instanceof CraftMetaArmorStand ? b : new CraftMetaArmorStand(b))
            .put("BANNER", (a, b) -> b instanceof CraftMetaBanner ? b : new CraftMetaBanner(b))
            .put("TILE_ENTITY", (a, b) -> new CraftMetaBlockState(b, a))
            .put("BOOK", (a, b) -> b != null && b.getClass().equals(CraftMetaBook.class) ? b : new CraftMetaBook(b))
            .put("BOOK_SIGNED", (a, b) -> b instanceof CraftMetaBookSigned ? b : new CraftMetaBookSigned(b))
            .put("SKULL", (a, b) -> b instanceof CraftMetaSkull ? b : new CraftMetaSkull(b))
            .put("LEATHER_ARMOR", (a, b) -> b instanceof CraftMetaLeatherArmor ? b : new CraftMetaLeatherArmor(b))
            .put("MAP", (a, b) -> b instanceof CraftMetaMap ? b : new CraftMetaMap(b))
            .put("POTION", (a, b) -> b instanceof CraftMetaPotion ? b : new CraftMetaPotion(b))
            .put("SPAWN_EGG", (a, b) -> b instanceof CraftMetaSpawnEgg ? b : new CraftMetaSpawnEgg(b))
            .put("ENCHANTED", (a, b) -> b instanceof CraftMetaEnchantedBook ? b : new CraftMetaEnchantedBook(b))
            .put("FIREWORK", (a, b) -> b instanceof CraftMetaFirework ? b : new CraftMetaFirework(b))
            .put("FIREWORK_EFFECT", (a, b) -> b instanceof CraftMetaCharge ? b : new CraftMetaCharge(b))
            .put("KNOWLEDGE_BOOK", (a, b) -> b instanceof CraftMetaKnowledgeBook ? b : new CraftMetaKnowledgeBook(b))
            .put("TROPICAL_FISH_BUCKET", (a, b) -> b instanceof CraftMetaTropicalFishBucket ? b : new CraftMetaTropicalFishBucket(b))
            .put("CROSSBOW", (a, b) -> b instanceof CraftMetaCrossbow ? b : new CraftMetaCrossbow(b))
            .put("SUSPICIOUS_STEW", (a, b) -> b instanceof CraftMetaSuspiciousStew ? b : new CraftMetaSuspiciousStew(b))
            .put("UNSPECIFIC", (a, b) -> new CraftMetaItem(b))
            .put("NULL", (a, b) -> null)
            .build();
    @Shadow
    @Mutable
    @Final
    public Class<?> data;
    // @formatter:off
    @Shadow @Mutable @Final private NamespacedKey key;
    @Shadow @Mutable @Final private Constructor<? extends MaterialData> ctor;
    // @formatter:on
    private MaterialPropertySpec.MaterialType freshwaterfish$type = MaterialPropertySpec.MaterialType.VANILLA;
    private MaterialPropertySpec freshwaterfish$spec;
    private boolean freshwaterfish$block = false, freshwaterfish$item = false;
    private Function<CraftMetaItem, ItemMeta> freshwaterfish$metaFunc;
    private Function<CraftBlock, BlockState> freshwaterfish$stateFunc;

    private static int tryGetMaxStackSize(Item item) {
        try {
            return item.getMaxStackSize(new ItemStack(item));
        } catch (Throwable t) {
            try {
                return item.getMaxStackSize();
            } catch (Throwable t1) {
                return 64;
            }
        }
    }

    private static int tryGetDurability(Item item) {
        try {
            return item.getMaxDamage(new ItemStack(item));
        } catch (Throwable t) {
            try {
                return item.getMaxDamage();
            } catch (Throwable t1) {
                return 0;
            }
        }
    }

    @Shadow
    public abstract boolean isBlock();

    @Override
    public void bridge$setBlock() {
        this.freshwaterfish$block = true;
    }

    @Override
    public void bridge$setItem() {
        this.freshwaterfish$item = true;
    }

    @Inject(method = "isBlock", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isBlock(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$type != MaterialPropertySpec.MaterialType.VANILLA) {
            cir.setReturnValue(freshwaterfish$block);
        }
    }

    @Inject(method = "isItem", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isItem(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$type != MaterialPropertySpec.MaterialType.VANILLA) {
            cir.setReturnValue(freshwaterfish$item);
        }
    }

    @Inject(method = "isEdible", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isEdible(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.edible);
        }
    }

    @Inject(method = "isRecord", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isRecord(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.record);
        }
    }

    @Inject(method = "isSolid", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isSolid(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.solid);
        }
    }

    @Inject(method = "isAir", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isAir(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.air);
        }
    }

    @Inject(method = "isTransparent", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isTransparent(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.transparent);
        }
    }

    @Inject(method = "isFlammable", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isFlammable(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.flammable);
        }
    }

    @Inject(method = "isBurnable", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isBurnable(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.burnable);
        }
    }

    @Inject(method = "isFuel", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isFuel(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.fuel);
        }
    }

    @Inject(method = "isOccluding", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isOccluding(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.occluding);
        }
    }

    @Inject(method = "hasGravity", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$hasGravity(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.gravity);
        }
    }

    @Inject(method = "isInteractable", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$isInteractable(CallbackInfoReturnable<Boolean> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.interactable);
        }
    }

    @Inject(method = "getHardness", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$getHardness(CallbackInfoReturnable<Float> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.hardness);
        }
    }

    @Inject(method = "getBlastResistance", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$getBlastResistance(CallbackInfoReturnable<Float> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.blastResistance);
        }
    }

    @Inject(method = "getCraftingRemainingItem", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$getCraftingRemainingItem(CallbackInfoReturnable<Material> cir) {
        if (freshwaterfish$spec != null && freshwaterfish$spec.craftingRemainingItem != null) {
            cir.setReturnValue(CraftMagicNumbers.getMaterial(ForgeRegistries.ITEMS.getValue(new ResourceLocation(freshwaterfish$spec.craftingRemainingItem))));
        }
    }

    @Inject(method = "getMaxStackSize", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        if (freshwaterfish$spec != null) {
            cir.setReturnValue(freshwaterfish$spec.maxStack);
        }
    }

    @Inject(method = "getMaxDurability", cancellable = true, at = @At("HEAD"))
    private void freshwaterfish$getMaxDurability(CallbackInfoReturnable<Short> cir) {
        if (freshwaterfish$spec != null && freshwaterfish$spec.maxDurability != null) {
            cir.setReturnValue(freshwaterfish$spec.maxDurability.shortValue());
        }
    }

    @Override
    public MaterialPropertySpec bridge$getSpec() {
        return freshwaterfish$spec;
    }

    @Override
    public MaterialPropertySpec.MaterialType bridge$getType() {
        return freshwaterfish$type;
    }

    @Override
    public Function<CraftMetaItem, ItemMeta> bridge$itemMetaFactory() {
        return freshwaterfish$metaFunc;
    }

    @Override
    public void bridge$setItemMetaFactory(Function<CraftMetaItem, ItemMeta> func) {
        this.freshwaterfish$metaFunc = func;
    }

    @Override
    public Function<CraftBlock, BlockState> bridge$blockStateFactory() {
        return freshwaterfish$stateFunc;
    }

    @Override
    public void bridge$setBlockStateFactory(Function<CraftBlock, BlockState> func) {
        this.freshwaterfish$stateFunc = func;
    }

    @Override
    public void bridge$setupBlock(ResourceLocation key, Block block, MaterialPropertySpec spec) {
        this.freshwaterfish$spec = spec.clone();
        freshwaterfish$type = MaterialPropertySpec.MaterialType.FORGE;
        freshwaterfish$block = true;
        freshwaterfish$setupCommon(key, block, block.asItem());
    }

    @Override
    public void bridge$setupVanillaBlock(MaterialPropertySpec spec) {
        if (spec != MaterialPropertySpec.EMPTY) {
            this.freshwaterfish$spec = spec.clone();
            this.setupBlockStateFunc();
        }
    }

    @Override
    public void bridge$setupItem(ResourceLocation key, Item item, MaterialPropertySpec spec) {
        this.freshwaterfish$spec = spec.clone();
        freshwaterfish$type = MaterialPropertySpec.MaterialType.FORGE;
        freshwaterfish$item = true;
        freshwaterfish$setupCommon(key, null, item);
    }

    @Override
    public boolean bridge$shouldApplyStateFactory() {
        return this.freshwaterfish$type != MaterialPropertySpec.MaterialType.VANILLA ||
                (this.freshwaterfish$spec != null && this.freshwaterfish$spec.blockStateClass != null);
    }

    @SuppressWarnings("unchecked")
    private void freshwaterfish$setupCommon(ResourceLocation key, Block block, Item item) {
        this.key = CraftNamespacedKey.fromMinecraft(key);
        if (freshwaterfish$spec.materialDataClass != null) {
            try {
                Class<?> data = Class.forName(freshwaterfish$spec.materialDataClass);
                if (MaterialData.class.isAssignableFrom(data)) {
                    this.data = data;
                    this.ctor = (Constructor<? extends MaterialData>) data.getConstructor(Material.class, byte.class);
                }
            } catch (Exception e) {
                FreshwaterFishMod.LOGGER.warn("material.bad-data-class", freshwaterfish$spec.materialDataClass, this);
                FreshwaterFishMod.LOGGER.warn(e);
            }
        }
        if (freshwaterfish$spec.maxStack == null) {
            freshwaterfish$spec.maxStack = tryGetMaxStackSize(item);
        }
        if (freshwaterfish$spec.maxDurability == null) {
            freshwaterfish$spec.maxDurability = tryGetDurability(item);
        }
        if (freshwaterfish$spec.edible == null) {
            freshwaterfish$spec.edible = false;
        }
        if (freshwaterfish$spec.record == null) {
            freshwaterfish$spec.record = false;
        }
        if (freshwaterfish$spec.solid == null) {
            freshwaterfish$spec.solid = block != null && block.defaultBlockState().canOcclude();
        }
        if (freshwaterfish$spec.air == null) {
            freshwaterfish$spec.air = block != null && block.defaultBlockState().isAir();
        }
        if (freshwaterfish$spec.transparent == null) {
            freshwaterfish$spec.transparent = block != null && block.defaultBlockState().useShapeForLightOcclusion();
        }
        if (freshwaterfish$spec.flammable == null) {
            freshwaterfish$spec.flammable = block != null && ((FireBlockBridge) Blocks.FIRE).bridge$canBurn(block);
        }
        if (freshwaterfish$spec.burnable == null) {
            freshwaterfish$spec.burnable = block != null && ((FireBlockBridge) Blocks.FIRE).bridge$canBurn(block);
        }
        if (freshwaterfish$spec.fuel == null) {
            freshwaterfish$spec.fuel = item != null && new ItemStack(item).getBurnTime(null) > 0;
        }
        if (freshwaterfish$spec.occluding == null) {
            freshwaterfish$spec.occluding = freshwaterfish$spec.solid;
        }
        if (freshwaterfish$spec.gravity == null) {
            freshwaterfish$spec.gravity = block instanceof FallingBlock;
        }
        if (freshwaterfish$spec.interactable == null) {
            freshwaterfish$spec.interactable = true;
        }
        if (freshwaterfish$spec.hardness == null) {
            freshwaterfish$spec.hardness = block != null ? block.defaultBlockState().destroySpeed : 0;
        }
        if (freshwaterfish$spec.blastResistance == null) {
            freshwaterfish$spec.blastResistance = block != null ? block.getExplosionResistance() : 0;
        }
        if (freshwaterfish$spec.craftingRemainingItem == null) {
            // noinspection deprecation
            freshwaterfish$spec.craftingRemainingItem = item != null && item.hasCraftingRemainingItem() ? ForgeRegistries.ITEMS.getKey(item.getCraftingRemainingItem()).toString() : null;
        }
        if (freshwaterfish$spec.itemMetaType == null) {
            freshwaterfish$spec.itemMetaType = "UNSPECIFIC";
        }
        BiFunction<Material, CraftMetaItem, ItemMeta> function = TYPES.get(freshwaterfish$spec.itemMetaType);
        if (function != null) {
            this.freshwaterfish$metaFunc = meta -> function.apply((Material) (Object) this, meta);
        } else {
            this.freshwaterfish$metaFunc = dynamicMetaCreator(freshwaterfish$spec.itemMetaType);
        }
        this.setupBlockStateFunc();
    }

    private void setupBlockStateFunc() {
        if (freshwaterfish$spec.blockStateClass != null && !freshwaterfish$spec.blockStateClass.equalsIgnoreCase("auto")) {
            try {
                Class<?> cl = Class.forName(freshwaterfish$spec.blockStateClass);
                if (!CraftBlockState.class.isAssignableFrom(cl)) {
                    throw LocalizedException.checked("registry.block-state.not-subclass", cl, CraftBlockState.class);
                }
                for (Constructor<?> constructor : cl.getDeclaredConstructors()) {
                    if (constructor.getParameterTypes().length == 1
                            && org.bukkit.block.Block.class.isAssignableFrom(constructor.getParameterTypes()[0])) {
                        constructor.setAccessible(true);
                        this.freshwaterfish$stateFunc = b -> {
                            try {
                                return (BlockState) constructor.newInstance(b);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        };
                    }
                }
            } catch (Exception e) {
                if (e instanceof LocalizedException) {
                    FreshwaterFishMod.LOGGER.warn(((LocalizedException) e).node(), ((LocalizedException) e).args());
                } else {
                    FreshwaterFishMod.LOGGER.warn("registry.block-state.error", this, freshwaterfish$spec.blockStateClass, e);
                }
            }
            if (this.freshwaterfish$stateFunc == null) {
                FreshwaterFishMod.LOGGER.warn("registry.block-state.no-candidate", this, freshwaterfish$spec.blockStateClass);
            }
        }
        if (this.freshwaterfish$stateFunc == null) {
            this.freshwaterfish$stateFunc = CraftBlockStates::getBlockState;
        }
    }

    private Function<CraftMetaItem, ItemMeta> dynamicMetaCreator(String type) {
        Function<CraftMetaItem, ItemMeta> candidate = null;
        try {
            Class<?> cl = Class.forName(type);
            if (!CraftMetaItem.class.isAssignableFrom(cl)) {
                throw LocalizedException.checked("registry.meta-type.not-subclass", cl, CraftMetaItem.class);
            }
            for (Constructor<?> constructor : cl.getDeclaredConstructors()) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length == 1) {
                    if (parameterTypes[0] == Material.class) {
                        constructor.setAccessible(true);
                        candidate = meta -> {
                            try {
                                return (ItemMeta) constructor.newInstance(this);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        };
                        break;
                    } else if (CraftMetaItem.class.isAssignableFrom(parameterTypes[0])) {
                        constructor.setAccessible(true);
                        candidate = meta -> {
                            try {
                                return (ItemMeta) constructor.newInstance(meta);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        };
                        break;
                    }
                } else if (parameterTypes.length == 2) {
                    if (parameterTypes[0] == Material.class && CraftMetaItem.class.isAssignableFrom(parameterTypes[1])) {
                        constructor.setAccessible(true);
                        candidate = meta -> {
                            try {
                                return (ItemMeta) constructor.newInstance(this, meta);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        };
                        break;
                    } else if (parameterTypes[1] == Material.class && CraftMetaItem.class.isAssignableFrom(parameterTypes[0])) {
                        constructor.setAccessible(true);
                        candidate = meta -> {
                            try {
                                return (ItemMeta) constructor.newInstance(meta, this);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        };
                        break;
                    }
                }
            }
        } catch (Exception e) {
            if (e instanceof LocalizedException) {
                FreshwaterFishMod.LOGGER.warn(((LocalizedException) e).node(), ((LocalizedException) e).args());
            } else {
                FreshwaterFishMod.LOGGER.warn("registry.meta-type.error", this, type, e);
            }
        }
        if (candidate == null) {
            FreshwaterFishMod.LOGGER.warn("registry.meta-type.no-candidate", this, type);
            candidate = CraftMetaItem::new;
        }
        return candidate;
    }
}
