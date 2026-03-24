package io.izzel.freshwaterfish.common.mod.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.izzel.freshwaterfish.common.mod.command.subcommands.AdventureTestSubCommand;
import io.izzel.freshwaterfish.common.mod.command.subcommands.InfoSubCommand;
import io.izzel.freshwaterfish.common.mod.command.subcommands.FreshwaterFishSubCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

// Main FreshwaterFish command system
public class FreshwaterFishCommand {

    private static final List<FreshwaterFishSubCommand> subCommands = new ArrayList<>();

    static {
        // Register default subcommands
        registerSubCommand(new InfoSubCommand());
        registerSubCommand(new AdventureTestSubCommand());
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("freshwaterfish")
                .requires(source -> source.hasPermission(2)) // Lower base permission
                .executes(FreshwaterFishCommand::showHelp)
                .then(Commands.literal("help")
                        .executes(FreshwaterFishCommand::showHelp));

        // Register all subcommands
        for (FreshwaterFishSubCommand subCommand : subCommands) {
            if (subCommand.isEnabled()) {
                command.then(subCommand.build());
            }
        }

        event.getDispatcher().register(command);
    }

    public static void registerSubCommand(FreshwaterFishSubCommand subCommand) {
        subCommands.add(subCommand);
    }

    private static int showHelp(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        source.sendSuccess(() -> Component.literal("§6=== FreshwaterFish Commands ==="), false);
        source.sendSuccess(() -> Component.literal("§7FreshwaterFish - Enhanced Minecraft server platform"), false);
        source.sendSuccess(() -> Component.literal(""), false);
        source.sendSuccess(() -> Component.literal("§e/luminara help §7- Show this help"), false);

        for (FreshwaterFishSubCommand subCommand : subCommands) {
            if (subCommand.isEnabled()) {
                String permissionInfo = subCommand.getRequiredPermissionLevel() > 2 ? " §c[OP]" : "";
                source.sendSuccess(() -> Component.literal("§e/luminara " + subCommand.getName() + permissionInfo + " §7- " + subCommand.getDescription()), false);
            }
        }

        source.sendSuccess(() -> Component.literal(""), false);
        source.sendSuccess(() -> Component.literal("§7Use §e/luminara <command> §7for more details"), false);
        source.sendSuccess(() -> Component.literal("§6========================"), false);
        return 1;
    }

}
