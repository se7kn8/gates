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

public class RSFlipFlop extends RedstoneDiodeBlock {

	public RSFlipFlop() {
		super(Properties.from(Blocks.REPEATER));
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, POWERED);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 0;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return side == state.get(TwoInputLogicGate.HORIZONTAL_FACING) || side == state.get(TwoInputLogicGate.HORIZONTAL_FACING).rotateY() || side == state.get(TwoInputLogicGate.HORIZONTAL_FACING).rotateYCCW();
	}

	@Override
	protected int calculateInputStrength(World world, BlockPos pos, BlockState state) {
		Direction facing = state.get(TwoInputLogicGate.HORIZONTAL_FACING);
		boolean set = getPowerOnSide(world, pos.offset(facing.rotateYCCW()), facing.rotateYCCW()) > 0;
		boolean reset = getPowerOnSide(world, pos.offset(facing.rotateY()), facing.rotateY()) > 0;

		if(set && reset){
			return 0;
		}

		if (set && !state.get(POWERED)) {
			//world.setBlockState(pos, state.with(SET, true));
			return 15;
		}

		if (reset && state.get(POWERED)) {
			//world.setBlockState(pos, state.with(SET, false));
			return 0;
		}

		return state.get(POWERED) ? 15 : 0;
	}

}
