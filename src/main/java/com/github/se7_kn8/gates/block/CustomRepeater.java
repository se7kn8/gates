package com.github.se7_kn8.gates.block;

import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CustomRepeater extends BlockRedstoneRepeater {

	private final int factor;

	public CustomRepeater(int delayFactor) {
		super(Properties.from(Blocks.REPEATER));
		this.factor = delayFactor;
	}

	@Override
	protected int getDelay(IBlockState p_196346_1_) {
		return p_196346_1_.get(DELAY) * factor;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockReader world, BlockPos pos, @Nullable EnumFacing side) {
		return side == state.get(HORIZONTAL_FACING) || side == state.get(HORIZONTAL_FACING).getOpposite();
	}
}
