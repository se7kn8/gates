package com.github.se7_kn8.gates.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CustomRepeater extends RepeaterBlock {

	private final int factor;

	public CustomRepeater(int delayFactor) {
		super(Properties.from(Blocks.REPEATER));
		this.factor = delayFactor;
	}

	@Override
	protected int getDelay(BlockState p_196346_1_) {
		return p_196346_1_.get(DELAY) * factor;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return side == state.get(HORIZONTAL_FACING) || side == state.get(HORIZONTAL_FACING).getOpposite();
	}
}
