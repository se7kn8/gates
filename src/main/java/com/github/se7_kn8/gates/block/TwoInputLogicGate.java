package com.github.se7_kn8.gates.block;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class TwoInputLogicGate extends DiodeBlock {

	private final BiFunction<Boolean, Boolean, Boolean> calculateOutputFunction;
	public static final BooleanProperty LEFT_INPUT = BooleanProperty.create("left");
	public static final BooleanProperty RIGHT_INPUT = BooleanProperty.create("right");

	public static final BiFunction<Boolean, Boolean, Boolean> AND_FUNCTION = (x, y) -> x && y;
	public static final BiFunction<Boolean, Boolean, Boolean> OR_FUNCTION = (x, y) -> x || y;
	public static final BiFunction<Boolean, Boolean, Boolean> NAND_FUNCTION = (x, y) -> !(x && y);
	public static final BiFunction<Boolean, Boolean, Boolean> NOR_FUNCTION = (x, y) -> !(x || y);
	public static final BiFunction<Boolean, Boolean, Boolean> XOR_FUNCTION = (x, y) -> x ^ y;
	public static final BiFunction<Boolean, Boolean, Boolean> XNOR_FUNCTION = (x, y) -> x == y;


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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, LEFT_INPUT, RIGHT_INPUT);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
		return side == state.getValue(FACING) || side == state.getValue(FACING).getClockWise() || side == state.getValue(FACING).getCounterClockWise();
	}

	@Override
	protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
		Direction facing = state.getValue(FACING);
		boolean firstInput = getAlternateSignalAt(level, pos.relative(facing.getCounterClockWise()), facing.getCounterClockWise()) > 0;
		boolean secondInput = getAlternateSignalAt(level, pos.relative(facing.getClockWise()), facing.getClockWise()) > 0;

		level.setBlockAndUpdate(pos, state.setValue(LEFT_INPUT, secondInput).setValue(RIGHT_INPUT, firstInput));

		return calculateOutputFunction.apply(firstInput, secondInput) ? 15 : 0;
	}

}
