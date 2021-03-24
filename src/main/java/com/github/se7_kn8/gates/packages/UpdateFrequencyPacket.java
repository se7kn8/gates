package com.github.se7_kn8.gates.packages;

import com.github.se7_kn8.gates.api.CapabilityUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateFrequencyPacket implements BasePacket{

	private final BlockPos pos;
	private final int frequency;

	public UpdateFrequencyPacket(BlockPos pos, int frequency) {
		this.pos = pos;
		this.frequency = frequency;
	}

	public UpdateFrequencyPacket(PacketBuffer buffer) {
		this.pos = buffer.readBlockPos();
		this.frequency = buffer.readInt();
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(this.pos);
		buffer.writeInt(this.frequency);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			World world = ctx.get().getSender().level;
			if (!world.isClientSide) {
				CapabilityUtil.findWirelessCapability(world, pos, c->{
					int newFrequency = this.frequency;

					if (newFrequency < c.getMinFrequency()) {
						newFrequency = c.getMinFrequency();
					}

					if(newFrequency > c.getMaxFrequency()){
						newFrequency = c.getMaxFrequency();
					}

					c.setFrequency(newFrequency);
				});
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
