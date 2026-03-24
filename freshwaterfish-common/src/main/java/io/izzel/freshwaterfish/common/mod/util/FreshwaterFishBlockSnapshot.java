package io.izzel.freshwaterfish.common.mod.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import org.bukkit.craftbukkit.v.block.CraftBlock;

public class FreshwaterFishBlockSnapshot extends CraftBlock {

    private final BlockState blockState;

    public FreshwaterFishBlockSnapshot(BlockSnapshot blockSnapshot, boolean current) {
        super(blockSnapshot.getLevel(), blockSnapshot.getPos());
        this.blockState = current ? blockSnapshot.getCurrentBlock() : blockSnapshot.getReplacedBlock();
    }

    public static FreshwaterFishBlockSnapshot fromBlockSnapshot(BlockSnapshot blockSnapshot, boolean current) {
        return new FreshwaterFishBlockSnapshot(blockSnapshot, current);
    }

    @Override
    public BlockState getNMS() {
        return blockState;
    }
}
