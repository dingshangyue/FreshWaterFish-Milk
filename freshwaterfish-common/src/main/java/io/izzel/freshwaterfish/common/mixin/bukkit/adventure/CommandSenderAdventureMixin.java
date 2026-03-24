package io.izzel.freshwaterfish.common.mixin.bukkit.adventure;

import io.izzel.freshwaterfish.common.adventure.PaperAdventure;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = CommandSender.class, remap = false)
public interface CommandSenderAdventureMixin extends Audience {

    @Override
    default void sendMessage(@NotNull Component message) {
        CommandSender sender = (CommandSender) this;
        // Enhanced message handling for better format preservation
        try {
            // If this is an FreshwaterFishDummyCommandSender, it has its own Adventure implementation
            if (sender instanceof io.izzel.freshwaterfish.common.mod.command.FreshwaterFishDummyCommandSender) {
                ((io.izzel.freshwaterfish.common.mod.command.FreshwaterFishDummyCommandSender) sender).sendMessage(message);
                return;
            }
            // Convert Adventure Component to legacy string and send
            String legacyMessage = PaperAdventure.adventureToLegacy(message);
            sender.sendMessage(legacyMessage);
        } catch (Exception e) {
            // Fallback to plain text
            String plainText = PaperAdventure.asPlain(message);
            sender.sendMessage(plainText);
        }
    }

    @Override
    default void sendMessage(@NotNull Component message, net.kyori.adventure.audience.MessageType type) {
        sendMessage(message);
    }

    @Override
    default void sendMessage(@NotNull Identity source, @NotNull Component message) {
        sendMessage(message);
    }

    @Override
    default void sendMessage(@NotNull Identity source, @NotNull Component message, net.kyori.adventure.audience.MessageType type) {
        sendMessage(message);
    }
}
