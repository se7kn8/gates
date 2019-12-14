package com.github.se7_kn8.gates.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.NonNullConsumer;

import javax.annotation.Nonnull;

public class CapabilityUtil {

	public static boolean findWirelessCapability(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull NonNullConsumer<IWirelessNode> c) {
		TileEntity entity = world.getTileEntity(pos);
		if (entity != null) {
			boolean isPresent = entity.getCapability(CapabilityWirelessNode.WIRELESS_NODE).isPresent();
			if(isPresent){
				entity.getCapability(CapabilityWirelessNode.WIRELESS_NODE).ifPresent(c);
				return true;
			}else{
				return false;
			}
		}else {
			return false;
		}
	}

}
