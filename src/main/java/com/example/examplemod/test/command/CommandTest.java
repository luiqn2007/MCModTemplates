package com.example.examplemod.test.command;

import com.example.examplemod.woodwork.Woodwork;
import com.example.examplemod.woodwork.WoodworkManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.NoSuchElementException;

import static com.example.examplemod.test.Registers.COMMANDS;

public class CommandTest {
    static {
        COMMANDS.register("test_tell", builder -> builder
                .execute(cs -> cs.getSource().sendSuccess(new TextComponent("Tell Me!"), true))
                .requires(css -> css.hasPermission(3)) // test by /execute
                .then("to")
                .then("player", EntityArgument.player())
                .then("message", StringArgumentType.string())
                .suggests((__, cb) -> cb.suggest("Hello").suggest("World").buildFuture())
                .execute(cs -> {
                    ServerPlayer target = EntityArgument.getPlayer(cs, "player");
                    String message = StringArgumentType.getString(cs, "message");
                    Entity sender = cs.getSource().getEntity();
                    if (sender == null) {
                        target.displayClientMessage(new TextComponent(message), false);
                    } else {
                        target.sendMessage(new TextComponent(message), ChatType.CHAT, sender.getUUID());
                    }
                })
                .forwardLiteral()
                .forward()
                .then("message")
                .then("msg", StringArgumentType.string())
                .execute(cs -> {
                    String msg = StringArgumentType.getString(cs, "msg");
                    cs.getSource().sendSuccess(new TextComponent(msg), true);
                }));

        COMMANDS.register("test_woodwork", builder -> builder
                .then("names")
                .execute(context -> {
                    for (ResourceLocation name : WoodworkManager.allNames()) {
                        context.getSource().sendSuccess(new TextComponent(name.toString() + ": " + WoodworkManager.getOrThrow(name).type), true);
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .forward("names")
                .forward()
                .then("types")
                .execute(context -> {
                    WoodType.values().forEach(type -> {
                        try {
                            Woodwork woodwork = WoodworkManager.getOrThrow(type);
                            context.getSource().sendSuccess(new TextComponent(type.name() + ": " + woodwork.name), true);
                        } catch (NoSuchElementException e) {
                            context.getSource().sendFailure(new TextComponent(type.name() + " not has a woodwork"));
                        }
                    });
                }));
    }

    public static void register() {

    }
}
