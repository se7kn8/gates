package com.github.se7_kn8.gates.packages;

import com.github.se7_kn8.gates.block.entity.RedstoneClockBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateRedstoneClockPacket implements BasePacket {

	private final BlockPos pos;
	private final int clockTime;
	private final int clockLength;

	public UpdateRedstoneClockPacket(BlockPos pos, int clockTime, int clockLength) {
		this.pos = pos;
		this.clockTime = clockTime;
		this.clockLength = clockLength;
	}

	public UpdateRedstoneClockPacket(FriendlyByteBuf buffer) {
		this.pos = buffer.readBlockPos();
		this.clockTime = buffer.readInt();
		this.clockLength = buffer.readInt();
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeInt(clockTime);
		buffer.writeInt(clockLength);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		BlockEntity entity = ctx.get().getSender().level.getBlockEntity(pos);
		if (entity instanceof RedstoneClockBlockEntity redstoneClockBlockEntity) {
			redstoneClockBlockEntity.setClockTime(clockTime);
			redstoneClockBlockEntity.setClockLength(clockLength);
			redstoneClockBlockEntity.resetClock();
		}
	}
}
