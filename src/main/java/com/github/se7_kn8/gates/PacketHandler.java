package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.packages.UpdateFrequencyPacket;
import com.github.se7_kn8.gates.packages.UpdatePortableTransmitterPacket;
import com.github.se7_kn8.gates.packages.UpdateRedstoneClockPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
	private static final String VERSION = "1";
	public static final SimpleChannel MOD_CHANNEL = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(Gates.MODID, "default_channel"))
			.clientAcceptedVersions(VERSION::equals)
			.serverAcceptedVersions(VERSION::equals)
			.networkProtocolVersion(() -> VERSION)
			.simpleChannel();

	public static void init() {
		int index = 0;
		MOD_CHANNEL.registerMessage(index++, UpdateFrequencyPacket.class, UpdateFrequencyPacket::encode, UpdateFrequencyPacket::new, UpdateFrequencyPacket::handle);
		MOD_CHANNEL.registerMessage(index++, UpdatePortableTransmitterPacket.class, UpdatePortableTransmitterPacket::encode, UpdatePortableTransmitterPacket::new, UpdatePortableTransmitterPacket::handle);
		MOD_CHANNEL.registerMessage(index++, UpdateRedstoneClockPacket.class, UpdateRedstoneClockPacket::encode, UpdateRedstoneClockPacket::new, UpdateRedstoneClockPacket::handle);
	}

}
