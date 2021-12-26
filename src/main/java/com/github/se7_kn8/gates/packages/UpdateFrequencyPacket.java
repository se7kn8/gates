package com.github.se7_kn8.gates.packages;

import com.github.se7_kn8.gates.api.CapabilityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateFrequencyPacket implements BasePacket{

	private final BlockPos pos;
	private final int frequency;

	public UpdateFrequencyPacket(BlockPos pos, int frequency) {
		this.pos = pos;
		this.frequency = frequency;
	}

	public UpdateFrequencyPacket(FriendlyByteBuf buffer) {
		this.pos = buffer.readBlockPos();
		this.frequency = buffer.readInt();
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(this.pos);
		buffer.writeInt(this.frequency);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Level level = ctx.get().getSender().level;
			if (!level.isClientSide) {
				CapabilityUtil.findWirelessCapability(level, pos, c->{
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
