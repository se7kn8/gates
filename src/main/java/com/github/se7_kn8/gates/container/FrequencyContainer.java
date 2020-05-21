package com.github.se7_kn8.gates.container;

import com.github.se7_kn8.gates.GatesContainers;
import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.api.IWirelessNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FrequencyContainer extends BasicPlayerContainer {

	public FrequencyContainer(int windowId, World world, BlockPos pos, PlayerInventory inventory, PlayerEntity player) {
		super(GatesContainers.FREQUENCY_CONTAINER_TYPE, windowId, world, pos, inventory, player);
		trackInt(new IntReferenceHolder() {
			@Override
			public int get() {
				return getFrequency();
			}

			@Override
			public void set(int value) {
				getTile().getCapability(CapabilityWirelessNode.WIRELESS_NODE).ifPresent(c -> c.setFrequency(value));
			}
		});
	}

	public int getFrequency() {
		return getTile().getCapability(CapabilityWirelessNode.WIRELESS_NODE).map(IWirelessNode::getFrequency).orElse(0);
	}
}
