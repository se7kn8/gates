package com.github.se7_kn8.gates.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class BasicPlayerContainer extends Container {

	private final TileEntity entity;
	private final PlayerEntity playerEntity;

	public BasicPlayerContainer(ContainerType<?> type, int windowId, World world, BlockPos pos, PlayerInventory inventory, PlayerEntity player) {
		super(type, windowId);
		this.entity = world.getTileEntity(pos);
		this.playerEntity = player;

		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
		}

		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}
	}

	public PlayerEntity getPlayer() {
		return playerEntity;
	}

	public TileEntity getTile() {
		return entity;
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
		return true;
	}
}
