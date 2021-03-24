package com.github.se7_kn8.gates.container;

import com.github.se7_kn8.gates.GatesContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

public class PortableRedstoneTransmitterContainer extends Container {

	public PortableRedstoneTransmitterContainer(int id) {
		super(GatesContainers.PORTABLE_TRANSMITTER_CONTAINER_TYPE, id);
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return true;
	}
}
