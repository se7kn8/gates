package com.github.se7_kn8.gates.block.entity;

import com.github.se7_kn8.gates.GatesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CustomDetectorBlockEntity extends BlockEntity {

	public CustomDetectorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(GatesBlocks.CUSTOM_DETECTOR_BLOCK_ENTITY_TYPE.get(), pWorldPosition, pBlockState);
	}
}
