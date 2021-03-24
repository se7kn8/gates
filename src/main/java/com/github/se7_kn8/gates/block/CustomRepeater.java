package com.github.se7_kn8.gates.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class CustomRepeater extends RepeaterBlock {

	private final int factor;

	public CustomRepeater(int delayFactor) {
		super(Properties.copy(Blocks.REPEATER));
		this.factor = delayFactor;
	}

	@Override
	protected int getDelay(BlockState p_196346_1_) {
		return p_196346_1_.getValue(DELAY) * factor;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return side == state.getValue(FACING) || side == state.getValue(FACING).getOpposite();
	}
}
