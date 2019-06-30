package com.github.se7_kn8.gates.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InstantRepeater extends RedstoneDiodeBlock {

	public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;

	public InstantRepeater() {
		super(Properties.from(Blocks.REPEATER));
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(LOCKED, Boolean.FALSE).with(POWERED, Boolean.FALSE));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState blockState = super.getStateForPlacement(context);
		return blockState.with(LOCKED, this.isLocked(context.getWorld(), context.getPos(), blockState));
	}

	@Override
	public boolean isLocked(IWorldReader worldIn, BlockPos pos, BlockState state) {
		return this.getPowerOnSides(worldIn, pos, state) > 0;
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return !worldIn.isRemote() && facing.getAxis() != stateIn.get(HORIZONTAL_FACING).getAxis() ? stateIn.with(LOCKED, this.isLocked(worldIn, currentPos, stateIn)) : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected int getDelay(@Nonnull BlockState p_196346_1_) {
		return 0;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return side == state.get(HORIZONTAL_FACING) || side == state.get(HORIZONTAL_FACING).getOpposite();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, POWERED, LOCKED);
	}
}
