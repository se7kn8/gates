package com.github.se7_kn8.gates.menu;

import com.github.se7_kn8.gates.GatesMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class PortableRedstoneTransmitterMenu extends AbstractContainerMenu {

	public PortableRedstoneTransmitterMenu(int id) {
		super(GatesMenus.PORTABLE_TRANSMITTER_MENU_TYPE.get(), id);
	}

	public PortableRedstoneTransmitterMenu(int id, Inventory inventory) {
		this(id);
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return true;
	}
}
