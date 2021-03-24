package com.github.se7_kn8.gates.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class WaxedRedstone extends RedstoneWireBlock implements IWaterLoggable {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public WaxedRedstone() {
		super(Properties.from(Blocks.REDSTONE_WIRE).sound(SoundType.HONEY));
		this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = super.getStateForPlacement(context);
		FluidState fluidState = context.getWorld().getFluidState(context.getPos());
		return state.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(WATERLOGGED);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		BlockState state = super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		return state.with(WATERLOGGED, worldIn.getFluidState(currentPos).getFluid() == Fluids.WATER);
	}
}
