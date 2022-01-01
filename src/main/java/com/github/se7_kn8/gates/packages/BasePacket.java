package com.github.se7_kn8.gates.packages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public interface BasePacket {

	void encode(FriendlyByteBuf buffer);

	void handle(Supplier<NetworkEvent.Context> ctx);

}
