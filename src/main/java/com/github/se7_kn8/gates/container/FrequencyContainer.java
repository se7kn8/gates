package com.github.se7_kn8.gates.container;

import com.github.se7_kn8.gates.GatesContainers;
import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.api.IWirelessNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public class FrequencyContainer extends Container {

	public TileEntity entity;
	private PlayerEntity playerEntity;

	public FrequencyContainer(int windowId, World world, BlockPos pos, PlayerInventory inventory, PlayerEntity player) {
		super(GatesContainers.TRANSMITTER_CONTAINER_TYPE, windowId);
		entity = world.getTileEntity(pos);
		this.playerEntity = player;

		trackInt(new IntReferenceHolder() {
			@Override
			public int get() {
				return getFrequency();
			}

			@Override
			public void set(int value) {
				entity.getCapability(CapabilityWirelessNode.WIRELESS_NODE).ifPresent(c -> c.setFrequency(value));
			}
		});

		for(int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
		}

		for(int y = 0; y < 3; ++y) {
			for(int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity p_82846_1_, int p_82846_2_) {
		return ItemStack.EMPTY;
	}

	public int getFrequency() {
		return entity.getCapability(CapabilityWirelessNode.WIRELESS_NODE).map(IWirelessNode::getFrequency).orElse(0);
	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
		return true;
	}
}
