package com.github.se7_kn8.gates.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class RSFlipFlop extends RedstoneDiodeBlock {

	public RSFlipFlop() {
		super(Properties.copy(Blocks.REPEATER));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 0;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return side == state.getValue(TwoInputLogicGate.FACING) || side == state.getValue(TwoInputLogicGate.FACING).getClockWise() || side == state.getValue(TwoInputLogicGate.FACING).getCounterClockWise();
	}

	@Override
	protected int getInputSignal(World world, BlockPos pos, BlockState state) {
		Direction facing = state.getValue(TwoInputLogicGate.FACING);
		boolean set = getAlternateSignalAt(world, pos.relative(facing.getCounterClockWise()), facing.getCounterClockWise()) > 0;
		boolean reset = getAlternateSignalAt(world, pos.relative(facing.getClockWise()), facing.getClockWise()) > 0;

		if(set && reset){
			return 0;
		}

		if (set && !state.getValue(POWERED)) {
			//world.setBlockState(pos, state.with(SET, true));
			return 15;
		}

		if (reset && state.getValue(POWERED)) {
			//world.setBlockState(pos, state.with(SET, false));
			return 0;
		}

		return state.getValue(POWERED) ? 15 : 0;
	}

}
