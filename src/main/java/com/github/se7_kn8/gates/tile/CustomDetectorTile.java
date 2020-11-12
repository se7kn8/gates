package com.github.se7_kn8.gates.tile;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.block.CustomDetector;
import net.minecraft.block.Block;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class CustomDetectorTile extends TileEntity implements ITickableTileEntity {

	public CustomDetectorTile() {
		super(GatesBlocks.RAIN_DETECTOR_TILE_ENTITY.get());
	}

	@Override
	public void tick() {
		if (this.world != null && !this.world.isRemote && this.world.getGameTime() % 20 == 0) {
			if (this.getBlockState().getBlock() instanceof CustomDetector) {
				Block block = this.getBlockState().getBlock();
				if (block instanceof CustomDetector) {
					((CustomDetector) block).updatePower(this.getBlockState(), this.world, this.pos);
				}
			}
		}
	}
}
