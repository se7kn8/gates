package com.github.se7_kn8.gates.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class CustomRepeater extends RepeaterBlock {

	private final int factor;

	public CustomRepeater(int delayFactor) {
		super(Properties.copy(Blocks.REPEATER));
		this.factor = delayFactor;
	}

	@Override
	protected int getDelay(BlockState state) {
		return state.getValue(DELAY) * factor;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
		return side == state.getValue(FACING) || side == state.getValue(FACING).getOpposite();
	}
}
