package com.github.se7_kn8.gates.block;

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

import net.minecraft.block.AbstractBlock.Properties;

public class TFlipFlop extends RedstoneDiodeBlock {

	public static final BooleanProperty SET = BooleanProperty.create("set");
	public static final BooleanProperty SET2 = BooleanProperty.create("set2");
	public static final BooleanProperty SHOULD_CHANGE = BooleanProperty.create("should_change");

	public TFlipFlop() {
		super(Properties.copy(Blocks.REPEATER));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(SET, false).setValue(SET2, false).setValue(SHOULD_CHANGE, false));
	}

	@Override
	protected int getDelay(BlockState state) {
		return 0;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, SET, SET2, SHOULD_CHANGE);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return side == state.getValue(TwoInputLogicGate.FACING) || side == state.getValue(TwoInputLogicGate.FACING).getOpposite();
	}

	@Override
	protected int getInputSignal(World worldIn, BlockPos pos, BlockState state) {
		// weird function
		Direction facing = state.getValue(TwoInputLogicGate.FACING);
		boolean oldSet = state.getValue(SET);
		boolean set = getAlternateSignalAt(worldIn, pos.relative(facing), facing) > 0;

		if (state.getValue(SHOULD_CHANGE)) {
			worldIn.setBlockAndUpdate(pos, state.setValue(SHOULD_CHANGE, false));
			return state.getValue(POWERED) ? 0 : 15;
		}

		if (oldSet != set) {
			boolean changeEdge = state.getValue(SET2);
			worldIn.setBlockAndUpdate(pos, state.setValue(SET, set).setValue(SET2, !changeEdge));
			if (!state.getValue(SET2)) {
				System.out.println("PULSE  !!!!!!!!!!!!!");
				worldIn.setBlockAndUpdate(pos, state.setValue(SHOULD_CHANGE, true));
				return state.getValue(POWERED) ? 0 : 15;
			}
		}

		return state.getValue(POWERED) ? 15 : 0;
	}
}
