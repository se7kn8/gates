package com.github.se7_kn8.gates.packages;

import com.github.se7_kn8.gates.api.CapabilityUtil;
import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateFrequencyPacket {

	private final BlockPos pos;
	private final int frequency;
	private final DimensionType type;

	public UpdateFrequencyPacket(BlockPos pos, int frequency, DimensionType type) {
		this.pos = pos;
		this.frequency = frequency;
		this.type = type;
	}

	public UpdateFrequencyPacket(PacketBuffer buffer) {
		this.pos = buffer.readBlockPos();
		this.frequency = buffer.readInt();
		this.type = DimensionType.getById(buffer.readInt());
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(this.pos);
		buffer.writeInt(this.frequency);
		buffer.writeInt(this.type.getId());
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			World world = ctx.get().getSender().world;
			if (!world.isRemote) {
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
