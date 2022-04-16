package com.example.examplemod.woodwork;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public record ModSignEditOpenPkt(BlockPos pos) {

    public static ModSignEditOpenPkt read(FriendlyByteBuf buf) {
        return new ModSignEditOpenPkt(buf.readBlockPos());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handleClient() {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.level != null && mc.level.getBlockEntity(pos) instanceof ModSignBlockEntity sign) {
            mc.setScreen(new ModSignEditScreen(sign, mc.isTextFilteringEnabled()));
        }
    }
}
