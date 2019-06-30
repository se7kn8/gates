package com.github.se7_kn8.gates.tile;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.api.IWirelessReceiver;
import net.minecraft.tileentity.TileEntity;

public class ReceiverTileEntity extends TileEntity implements IWirelessReceiver {

	public ReceiverTileEntity() {
		super(GatesBlocks.RECEIVER_TILE_ENTITY_TYPE);
	}

	@Override
	public void onReceive(int power) {
		System.out.println("Received: " + power);
	}

	@Override
	public int getFrequency() {
		return 1234;
	}
}
