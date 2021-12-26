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
import java.util.function.Function;

public class OneInputLogicGate extends DiodeBlock {
	private Function<Boolean, Boolean> calculateOutputFunction;

	public static final Function<Boolean, Boolean> NOT_FUNCTION = x1 -> !x1;

	public static final BooleanProperty INPUT = BooleanProperty.create("input");

	public OneInputLogicGate(Function<Boolean, Boolean> calculateOutputFunction) {
		super(Properties.copy(Blocks.REPEATER));
		this.calculateOutputFunction = calculateOutputFunction;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(INPUT, false));
	}

	@Override
	protected int getDelay(@Nonnull BlockState state) {
		return 0;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, POWERED, INPUT);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
		return side == state.getValue(TwoInputLogicGate.FACING) || side == state.getValue(TwoInputLogicGate.FACING).getOpposite();
	}

	@Override
	protected int getInputSignal(Level pLevel, BlockPos pPos, BlockState pState) {
		Direction facing = pState.getValue(FACING);
		boolean firstInput = getAlternateSignalAt(pLevel, pPos.relative(facing), facing) > 0;

		pLevel.setBlockAndUpdate(pPos, pState.setValue(INPUT, firstInput));
		return calculateOutputFunction.apply(firstInput) ? 15 : 0;
	}
}
