package com.github.se7_kn8.gates.packages;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface BasePacket {

	void encode(PacketBuffer buffer);

	void handle(Supplier<NetworkEvent.Context> ctx);

}
