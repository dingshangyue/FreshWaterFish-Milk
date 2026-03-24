package io.izzel.freshwaterfish.common.mod.command.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

/**
 * FreshwaterFish subcommand interface
 */
public interface FreshwaterFishSubCommand {

    String getName();

    String getDescription();

    LiteralArgumentBuilder<CommandSourceStack> build();

    default int getRequiredPermissionLevel() {
        return 3;
    }

    default boolean isEnabled() {
        return true;
    }
}
