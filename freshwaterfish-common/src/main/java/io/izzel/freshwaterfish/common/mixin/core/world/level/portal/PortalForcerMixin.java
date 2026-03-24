package io.izzel.freshwaterfish.common.mixin.core.world.level.portal;

import io.izzel.freshwaterfish.common.bridge.core.entity.EntityBridge;
import io.izzel.freshwaterfish.common.bridge.core.world.TeleporterBridge;
import io.izzel.freshwaterfish.common.bridge.core.world.WorldBridge;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.PortalForcer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v.CraftWorld;
import org.bukkit.craftbukkit.v.util.BlockStateListPopulator;
import org.bukkit.event.world.PortalCreateEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin implements TeleporterBridge {

    @Shadow
    @Final
    protected ServerLevel level;
    private transient int freshwaterfish$searchRadius = -1;
    private transient BlockStateListPopulator freshwaterfish$populator;
    // @formatter:on
    private transient Entity freshwaterfish$entity;
    private transient int freshwaterfish$createRadius = -1;

    // @formatter:off
    @Shadow public abstract Optional<BlockUtil.FoundRectangle> createPortal(BlockPos pos, Direction.Axis axis);

    @Shadow public abstract Optional<BlockUtil.FoundRectangle> findPortalAround(BlockPos p_192986_, boolean p_192987_, WorldBorder p_192988_);

    @ModifyArg(method = "findPortalAround", 
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/village/poi/PoiManager;ensureLoadedAndValid(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;I)V", remap = false),
        index = 2, remap = false)
    private int freshwaterfish$useSearchRadius(int i) {
        return this.freshwaterfish$searchRadius == -1 ? i : this.freshwaterfish$searchRadius;
    }

    public Optional<BlockUtil.FoundRectangle> findPortalAround(BlockPos pos, WorldBorder worldBorder, int searchRadius) {
        this.freshwaterfish$searchRadius = searchRadius;
        try {
            return this.findPortalAround(pos, false, worldBorder);
        } finally {
            this.freshwaterfish$searchRadius = -1;
        }
    }

    @Override
    public Optional<BlockUtil.FoundRectangle> bridge$findPortal(BlockPos pos, WorldBorder worldborder, int searchRadius) {
        return findPortalAround(pos, worldborder, searchRadius);
    }

    @ModifyArg(method = "createPortal", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;spiralAround(Lnet/minecraft/core/BlockPos;ILnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;)Ljava/lang/Iterable;"))
    private int freshwaterfish$changeRadius(int i) {
        return this.freshwaterfish$createRadius == -1 ? i : this.freshwaterfish$createRadius;
    }

    @Redirect(method = "createPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private boolean freshwaterfish$captureBlocks1(ServerLevel serverWorld, BlockPos pos, BlockState state) {
        if (this.freshwaterfish$populator == null) {
            this.freshwaterfish$populator = new BlockStateListPopulator(serverWorld);
        }
        return this.freshwaterfish$populator.setBlock(pos, state, 3);
    }

    @Redirect(method = "createPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean freshwaterfish$captureBlocks2(ServerLevel serverWorld, BlockPos pos, BlockState state, int flags) {
        if (this.freshwaterfish$populator == null) {
            this.freshwaterfish$populator = new BlockStateListPopulator(serverWorld);
        }
        return this.freshwaterfish$populator.setBlock(pos, state, flags);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(method = "createPortal", cancellable = true, at = @At("RETURN"))
    private void freshwaterfish$portalCreate(BlockPos pos, Direction.Axis axis, CallbackInfoReturnable<Optional<BlockUtil.FoundRectangle>> cir) {
        CraftWorld craftWorld = ((WorldBridge) this.level).bridge$getWorld();
        List<org.bukkit.block.BlockState> blockStates;
        if (this.freshwaterfish$populator == null) {
            blockStates = new ArrayList<>();
        } else {
            blockStates = (List) this.freshwaterfish$populator.getList();
        }
        PortalCreateEvent event = new PortalCreateEvent(blockStates, craftWorld, (this.freshwaterfish$entity == null) ? null : ((EntityBridge) this.freshwaterfish$entity).bridge$getBukkitEntity(), PortalCreateEvent.CreateReason.NETHER_PAIR);

        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            cir.setReturnValue(Optional.empty());
            return;
        }
        if (this.freshwaterfish$populator != null) {
            this.freshwaterfish$populator.updateList();
        }
    }

    public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos pos, Direction.Axis axis, Entity entity, int createRadius) {
        this.freshwaterfish$entity = entity;
        this.freshwaterfish$createRadius = createRadius;
        try {
            return this.createPortal(pos, axis);
        } finally {
            this.freshwaterfish$entity = null;
            this.freshwaterfish$createRadius = -1;
        }
    }

    @Override
    public Optional<BlockUtil.FoundRectangle> bridge$createPortal(BlockPos pos, Direction.Axis axis, Entity entity, int createRadius) {
        return createPortal(pos, axis, entity, createRadius);
    }
}
