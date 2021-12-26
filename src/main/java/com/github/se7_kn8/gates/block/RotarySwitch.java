package com.github.se7_kn8.gates.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RotarySwitch extends FaceAttachedHorizontalDirectionalBlock {

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
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		switch (pState.getValue(FACE)) {
			case FLOOR:
				switch (pState.getValue(FACING).getAxis()) {
					case X:
						return FLOOR_X_SHAPE;
					case Z:
					default:
						return FLOOR_Z_SHAPE;
				}
			case WALL:
				switch (pState.getValue(FACING)) {
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
				switch (pState.getValue(FACING).getAxis()) {
					case X:
						return CEILING_X_SHAPE;
					case Z:
					default:
						return CEILING_Z_SHAPE;
				}
		}
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!pLevel.isClientSide()) {
			BlockState newState;
			if (pPlayer.isShiftKeyDown()) {
				int power = pState.getValue(POWER);
				if (power == 0) {
					power = 16;
				}
				power -= 1;
				newState = pState.setValue(POWER, power);
			} else {
				newState = pState.cycle(POWER);
			}
			pLevel.playSound(null, pPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.6f);
			pLevel.setBlockAndUpdate(pPos, newState);
			pLevel.updateNeighborsAt(pPos, this);
			pLevel.updateNeighborsAt(pPos.relative(getConnectedDirection(newState).getOpposite()), this);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
		return pState.getValue(POWER);
	}

	@Override
	public int getDirectSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
		if (getConnectedDirection(pState) == pDirection) {
			return pState.getValue(POWER);
		}
		return 0;
	}

	@Override
	public boolean isSignalSource(BlockState pState) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACE, FACING, POWER);
	}
}
