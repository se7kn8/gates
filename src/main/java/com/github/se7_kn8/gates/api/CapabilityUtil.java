package com.github.se7_kn8.gates.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.NonNullConsumer;

import javax.annotation.Nonnull;

public class CapabilityUtil {

	public static void findWirelessCapability(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull NonNullConsumer<IWirelessNode> c) {
		TileEntity entity = world.getTileEntity(pos);
		if (entity != null) {
			entity.getCapability(CapabilityWirelessNode.WIRELESS_NODE).ifPresent(c);
		}
	}

}
