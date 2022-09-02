package com.github.se7_kn8.gates.menu;

import com.github.se7_kn8.gates.GatesMenus;
import com.github.se7_kn8.gates.api.IWirelessNode;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
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

		for(int l = 0; l < 3; ++l) {
			for(int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(inventory, k + l * 9 + 9, 8 + k * 18, 84 + l * 18));
			}
		}

		for(int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
		}
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
