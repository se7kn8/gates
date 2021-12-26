package com.github.se7_kn8.gates.block;
/*
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TFlipFlop extends RedstoneDiodeBlock {

	public static final BooleanProperty SET = BooleanProperty.create("set");
	public static final BooleanProperty SET2 = BooleanProperty.create("set2");
	public static final BooleanProperty SHOULD_CHANGE = BooleanProperty.create("should_change");

	public TFlipFlop() {
		super(Properties.from(Blocks.REPEATER));
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, false).with(SET, false).with(SET2, false).with(SHOULD_CHANGE, false));
	}

	@Override
	protected int getDelay(BlockState state) {
		return 0;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, POWERED, SET, SET2, SHOULD_CHANGE);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return side == state.get(TwoInputLogicGate.HORIZONTAL_FACING) || side == state.get(TwoInputLogicGate.HORIZONTAL_FACING).getOpposite();
	}

	@Override
	protected int calculateInputStrength(World worldIn, BlockPos pos, BlockState state) {
		// weird function
		Direction facing = state.get(TwoInputLogicGate.HORIZONTAL_FACING);
		boolean oldSet = state.get(SET);
		boolean set = getPowerOnSide(worldIn, pos.offset(facing), facing) > 0;

		if (state.get(SHOULD_CHANGE)) {
			worldIn.setBlockState(pos, state.with(SHOULD_CHANGE, false));
			return state.get(POWERED) ? 0 : 15;
		}

		if (oldSet != set) {
			boolean changeEdge = state.get(SET2);
			worldIn.setBlockState(pos, state.with(SET, set).with(SET2, !changeEdge));
			if (!state.get(SET2)) {
				System.out.println("PULSE  !!!!!!!!!!!!!");
				worldIn.setBlockState(pos, state.with(SHOULD_CHANGE, true));
				return state.get(POWERED) ? 0 : 15;
			}
		}

		return state.get(POWERED) ? 15 : 0;
	}
}
*/
