package com.github.se7_kn8.gates.container;

import com.github.se7_kn8.gates.GatesContainers;
import com.github.se7_kn8.gates.tile.RedstoneClockBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AdvancedRedstoneClockMenu extends AbstractContainerMenu {

	private final ContainerData data;

	public AdvancedRedstoneClockMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainerData(2));
	}

	public AdvancedRedstoneClockMenu(int id, Inventory inventory, ContainerData data) {
		super(GatesContainers.ADVANCED_REDSTONE_CLOCK_MENU_TYPE, id);
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


	public int getClockTime() {
		return this.data.get(RedstoneClockBlockEntity.CLOCK_TIME_INDEX);
	}

	public int getClockLength() {
		return this.data.get(RedstoneClockBlockEntity.CLOCK_LENGTH_INDEX);
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
