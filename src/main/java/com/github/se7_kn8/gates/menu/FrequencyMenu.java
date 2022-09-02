package com.github.se7_kn8.gates.menu;

import com.github.se7_kn8.gates.GatesMenus;
import com.github.se7_kn8.gates.api.IWirelessNode;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class FrequencyMenu extends AbstractContainerMenu {

	private final ContainerData data;

	public FrequencyMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainerData(3));
	}

	public FrequencyMenu(int id, Inventory inventory, ContainerData data) {
		super(GatesMenus.FREQUENCY_MENU_TYPE.get(), id);
		this.data = data;

		this.addDataSlots(data);
	}

	public int getFrequency() {
		return this.data.get(IWirelessNode.FREQUENCY_INDEX);
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		return ItemStack.EMPTY;
	}
}
