package io.izzel.freshwaterfish.common.mixin.core.commands;

import io.izzel.freshwaterfish.common.bridge.core.command.ICommandSourceBridge;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandSource.class)
public interface CommandSourceMixin extends ICommandSourceBridge {

    default CommandSender getBukkitSender(CommandSourceStack wrapper) {
        return this.bridge$getBukkitSender(wrapper);
    }
}
