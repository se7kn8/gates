package com.github.se7_kn8.gates.container;

import com.github.se7_kn8.gates.GatesContainers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PortableRedstoneTransmitterContainer extends AbstractContainerMenu {

	public PortableRedstoneTransmitterContainer(int id) {
		super(GatesContainers.PORTABLE_TRANSMITTER_MENU_TYPE, id);
	}

	public PortableRedstoneTransmitterContainer(int id, Inventory inventory) {
		this(id);
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return true;
	}
}
