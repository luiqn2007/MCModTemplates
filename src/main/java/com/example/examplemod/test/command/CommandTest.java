package com.example.examplemod.test.command;

import com.example.examplemod.command.CommandRegister;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class CommandTest {

    public static final CommandRegister REGISTER = new CommandRegister();

    public static final String CMD_SEND_MESSAGE = REGISTER.register("test_tell", builder -> builder
            .execute(cs -> cs.getSource().sendSuccess(new TextComponent("Tell Me!"), true))
            .then("to")
            .then("player", EntityArgument.player())
            .then("message", StringArgumentType.string())
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
}
