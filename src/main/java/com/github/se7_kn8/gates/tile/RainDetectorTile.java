package com.github.se7_kn8.gates.tile;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.block.RainDetector;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class RainDetectorTile extends TileEntity implements ITickableTileEntity {

	public RainDetectorTile() {
		super(GatesBlocks.RAIN_DETECTOR_TILE_ENTITY);
	}

	@Override
	public void tick() {
		if (this.world != null && !this.world.isRemote && this.world.getGameTime() % 20 == 0) {
			if (this.getBlockState().getBlock() instanceof RainDetector) {
				RainDetector.updatePower(this.getBlockState(), this.world, this.pos);
			}
		}
	}
}
