package com.github.se7_kn8.gates.block;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.block.entity.CustomDetectorBlockEntity;
import com.github.se7_kn8.gates.util.TriFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CustomDetector extends BaseEntityBlock {

	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
	public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);

	public static final TriFunction<BlockState, Level, BlockPos, Integer> RAIN_DETECTOR_FUNCTION = (blockState, level, blockPos) -> level.isRainingAt(blockPos.above(2)) ? 15 : 0;
	public static final TriFunction<BlockState, Level, BlockPos, Integer> THUNDER_DETECTOR_FUNCTION = (blockState, level, blockPos) -> level.isThundering() ? 15 : 0;

	private final TriFunction<BlockState, Level, BlockPos, Integer> activateFunction;

	public CustomDetector(TriFunction<BlockState, Level, BlockPos, Integer> activateFunction) {
		super(Properties.copy(Blocks.DAYLIGHT_DETECTOR));
		this.activateFunction = activateFunction;
		this.registerDefaultState(this.stateDefinition.any().setValue(INVERTED, false).setValue(POWER, 0));
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	@Override
	public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
		return pState.getValue(POWER);
	}

	private static void tickEntity(Level level, BlockPos pos, BlockState state, CustomDetectorBlockEntity tile) {
		if (level.getGameTime() % 20 == 0) {
			Block block = state.getBlock();
			if (block instanceof CustomDetector) {
				((CustomDetector) block).updatePower(state, level, pos);

			}
		}
	}

	public void updatePower(BlockState state, Level level, BlockPos pos) {
		int newPower = activateFunction.apply(state, level, pos);

		if (state.getValue(INVERTED)) {
			newPower = 15 - newPower;
		}

		if (newPower != state.getValue(POWER)) {
			level.setBlockAndUpdate(pos, state.setValue(POWER, newPower));
		}

	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pPlayer.mayBuild()) {
			if (pLevel.isClientSide) {
				return InteractionResult.SUCCESS;
			} else {
				BlockState newState = pState.cycle(INVERTED);
				pLevel.setBlock(pPos, newState, 4);
				updatePower(pState, pLevel, pPos);
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}


	@Override
	public boolean isSignalSource(BlockState pState) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(POWER, INVERTED);
	}


	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new CustomDetectorBlockEntity(pPos, pState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, GatesBlocks.CUSTOM_DETECTOR_BLOCK_ENTITY_TYPE.get(), CustomDetector::tickEntity) : null;
	}
}
