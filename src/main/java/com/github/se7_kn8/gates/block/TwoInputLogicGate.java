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
		super(Block.Properties.from(Blocks.REPEATER));
		this.calculateOutputFunction = calculateOutputFunction;
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, false).with(LEFT_INPUT, false).with(RIGHT_INPUT, false));
	}

	@Override
	protected int getDelay(@Nonnull BlockState state) {
		return 0;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, POWERED, LEFT_INPUT, RIGHT_INPUT);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return side == state.get(TwoInputLogicGate.HORIZONTAL_FACING) || side == state.get(TwoInputLogicGate.HORIZONTAL_FACING).rotateY() || side == state.get(TwoInputLogicGate.HORIZONTAL_FACING).rotateYCCW();
	}

	@Override
	protected int calculateInputStrength(World world, BlockPos pos, BlockState state) {
		Direction facing = state.get(TwoInputLogicGate.HORIZONTAL_FACING);
		boolean firstInput = getPowerOnSide(world, pos.offset(facing.rotateYCCW()), facing.rotateYCCW()) > 0;
		boolean secondInput = getPowerOnSide(world, pos.offset(facing.rotateY()), facing.rotateY()) > 0;

		world.setBlockState(pos, state.with(LEFT_INPUT, secondInput).with(RIGHT_INPUT, firstInput));

		return calculateOutputFunction.apply(firstInput, secondInput) ? 15 : 0;
	}

}
