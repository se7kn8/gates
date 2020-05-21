package com.github.se7_kn8.gates.packages;

import com.github.se7_kn8.gates.tile.RedstoneClockTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

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

	public UpdateRedstoneClockPacket(PacketBuffer buffer) {
		this.pos = buffer.readBlockPos();
		this.clockTime = buffer.readInt();
		this.clockLength = buffer.readInt();
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeInt(clockTime);
		buffer.writeInt(clockLength);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		TileEntity tile = ctx.get().getSender().world.getTileEntity(pos);
		if (tile instanceof RedstoneClockTileEntity) {
			RedstoneClockTileEntity redstoneClockTileEntity = (RedstoneClockTileEntity) tile;
			redstoneClockTileEntity.setClockTime(clockTime);
			redstoneClockTileEntity.setClockLength(clockLength);
			redstoneClockTileEntity.resetClock();
		}
	}
}
