package com.Uniye.Uniyesmod.network;


import com.Uniye.Uniyesmod.Item.GravityCoreItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DoubleJumpPacket {
    public static void encode(DoubleJumpPacket msg, FriendlyByteBuf buf) {}
    public static DoubleJumpPacket decode(FriendlyByteBuf buf) {
        return new DoubleJumpPacket();
    }

    public static void handle(DoubleJumpPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                GravityCoreItem.jump(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
