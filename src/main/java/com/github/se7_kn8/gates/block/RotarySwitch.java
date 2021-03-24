package com.github.se7_kn8.gates.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import net.minecraft.block.AbstractBlock.Properties;

public class RotarySwitch extends HorizontalFaceBlock {

	public static final IntegerProperty POWER = BlockStateProperties.POWER;

	protected static final VoxelShape LEVER_NORTH_AABB = Block.box(5.0D, 4.0D, 10.0D, 11.0D, 12.0D, 16.0D);
	protected static final VoxelShape LEVER_SOUTH_AABB = Block.box(5.0D, 4.0D, 0.0D, 11.0D, 12.0D, 6.0D);
	protected static final VoxelShape LEVER_WEST_AABB = Block.box(10.0D, 4.0D, 5.0D, 16.0D, 12.0D, 11.0D);
	protected static final VoxelShape LEVER_EAST_AABB = Block.box(0.0D, 4.0D, 5.0D, 6.0D, 12.0D, 11.0D);
	protected static final VoxelShape FLOOR_Z_SHAPE = Block.box(5.0D, 0.0D, 4.0D, 11.0D, 6.0D, 12.0D);
	protected static final VoxelShape FLOOR_X_SHAPE = Block.box(4.0D, 0.0D, 5.0D, 12.0D, 6.0D, 11.0D);
	protected static final VoxelShape CEILING_Z_SHAPE = Block.box(5.0D, 10.0D, 4.0D, 11.0D, 16.0D, 12.0D);
	protected static final VoxelShape CEILING_X_SHAPE = Block.box(4.0D, 10.0D, 5.0D, 12.0D, 16.0D, 11.0D);


	public RotarySwitch() {
		super(Properties.copy(Blocks.LEVER));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWER, 0).setValue(FACE, AttachFace.WALL));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch(state.getValue(FACE)) {
			case FLOOR:
				switch(state.getValue(FACING).getAxis()) {
					case X:
						return FLOOR_X_SHAPE;
					case Z:
					default:
						return FLOOR_Z_SHAPE;
				}
			case WALL:
				switch(state.getValue(FACING)) {
					case EAST:
						return LEVER_EAST_AABB;
					case WEST:
						return LEVER_WEST_AABB;
					case SOUTH:
						return LEVER_SOUTH_AABB;
					case NORTH:
					default:
						return LEVER_NORTH_AABB;
				}
			case CEILING:
			default:
				switch(state.getValue(FACING).getAxis()) {
					case X:
						return CEILING_X_SHAPE;
					case Z:
					default:
						return CEILING_Z_SHAPE;
				}
		}
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isClientSide) {
			BlockState newState;
			if (player.isShiftKeyDown()) {
				int power = state.getValue(POWER);
				if (power == 0) {
					power = 16;
				}
				power -= 1;
				newState = state.setValue(POWER, power);
			} else {
				newState = state.cycle(POWER);
			}
			worldIn.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6f);
			worldIn.setBlockAndUpdate(pos, newState);
			worldIn.updateNeighborsAt(pos, this);
			worldIn.updateNeighborsAt(pos.relative(getConnectedDirection(newState).getOpposite()), this);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return blockState.getValue(POWER);
	}

	@Override
	public int getDirectSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		if (getConnectedDirection(blockState) == side) {
			return blockState.getValue(POWER);
		}
		return 0;
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACE, FACING, POWER);
	}

}
