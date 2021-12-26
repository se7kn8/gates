package com.github.se7_kn8.gates.api;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.NonNullConsumer;

import javax.annotation.Nonnull;

public class CapabilityUtil {

	public static boolean findWirelessCapability(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull NonNullConsumer<IWirelessNode> c) {
		BlockEntity entity = level.getBlockEntity(pos);
		if (entity != null) {
			boolean isPresent = entity.getCapability(CapabilityWirelessNode.WIRELESS_NODE).isPresent();
			if (isPresent) {
				entity.getCapability(CapabilityWirelessNode.WIRELESS_NODE).ifPresent(c);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
