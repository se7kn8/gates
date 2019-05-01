package com.github.se7_kn8.gates.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InstantRepeater extends BlockRedstoneDiode {

	public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;

	public InstantRepeater() {
		super(Properties.from(Blocks.REPEATER));
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, EnumFacing.NORTH).with(LOCKED, Boolean.FALSE).with(POWERED, Boolean.FALSE));
	}

	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		IBlockState iblockstate = super.getStateForPlacement(context);
		return iblockstate.with(LOCKED, this.isLocked(context.getWorld(), context.getPos(), iblockstate));
	}

	@Override
	public boolean isLocked(IWorldReaderBase worldIn, BlockPos pos, IBlockState state) {
		return this.getPowerOnSides(worldIn, pos, state) > 0;
	}

	@Override
	public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return !worldIn.isRemote() && facing.getAxis() != stateIn.get(HORIZONTAL_FACING).getAxis() ? stateIn.with(LOCKED, this.isLocked(worldIn, currentPos, stateIn)) : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected int getDelay(@Nonnull IBlockState p_196346_1_) {
		return 0;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockReader world, BlockPos pos, @Nullable EnumFacing side) {
		return side == state.get(HORIZONTAL_FACING) || side == state.get(HORIZONTAL_FACING).getOpposite();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(HORIZONTAL_FACING, POWERED, LOCKED);
	}
}
