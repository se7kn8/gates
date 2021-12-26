package com.github.se7_kn8.gates.tile;

import com.github.se7_kn8.gates.GatesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CustomDetectorTile extends BlockEntity {

	public CustomDetectorTile(BlockPos pWorldPosition, BlockState pBlockState) {
		super(GatesBlocks.RAIN_DETECTOR_TILE_ENTITY.get(), pWorldPosition, pBlockState);
	}
}
