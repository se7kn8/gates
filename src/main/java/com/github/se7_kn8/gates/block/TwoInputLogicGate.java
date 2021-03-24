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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class TwoInputLogicGate extends RedstoneDiodeBlock {

	private BiFunction<Boolean, Boolean, Boolean> calculateOutputFunction;
	public static final BooleanProperty LEFT_INPUT = BooleanProperty.create("left");
	public static final BooleanProperty RIGHT_INPUT = BooleanProperty.create("right");

	public TwoInputLogicGate(BiFunction<Boolean, Boolean, Boolean> calculateOutputFunction) {
		super(Block.Properties.copy(Blocks.REPEATER));
		this.calculateOutputFunction = calculateOutputFunction;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(LEFT_INPUT, false).setValue(RIGHT_INPUT, false));
	}

	@Override
	protected int getDelay(@Nonnull BlockState state) {
		return 0;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, LEFT_INPUT, RIGHT_INPUT);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return side == state.getValue(TwoInputLogicGate.FACING) || side == state.getValue(TwoInputLogicGate.FACING).getClockWise() || side == state.getValue(TwoInputLogicGate.FACING).getCounterClockWise();
	}

	@Override
	protected int getInputSignal(World world, BlockPos pos, BlockState state) {
		Direction facing = state.getValue(TwoInputLogicGate.FACING);
		boolean firstInput = getAlternateSignalAt(world, pos.relative(facing.getCounterClockWise()), facing.getCounterClockWise()) > 0;
		boolean secondInput = getAlternateSignalAt(world, pos.relative(facing.getClockWise()), facing.getClockWise()) > 0;

		world.setBlockAndUpdate(pos, state.setValue(LEFT_INPUT, secondInput).setValue(RIGHT_INPUT, firstInput));

		return calculateOutputFunction.apply(firstInput, secondInput) ? 15 : 0;
	}

}
