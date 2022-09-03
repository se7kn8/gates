package com.github.se7_kn8.gates.block.wireless_redstone;

import com.github.se7_kn8.gates.api.CapabilityUtil;
import com.github.se7_kn8.gates.api.IHighlightInfoBlock;
import com.github.se7_kn8.gates.block.entity.TransmitterBlockEntity;
import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import com.github.se7_kn8.gates.util.Utils;
import com.github.se7_kn8.gates.util.WirelessRedstoneUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class TransmitterBlock extends BaseEntityBlock implements IHighlightInfoBlock {

	public static final VoxelShape SHAPE = Shapes.or(Utils.GATE_SHAPE, Block.box(7.0D, 0.0D, 7.0D, 9.0D, 10.0D, 9.0D));

	public static IntegerProperty POWER = BlockStateProperties.POWER;

	public TransmitterBlock() {
		super(Properties.copy(Blocks.REPEATER));
		this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0));
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		return WirelessRedstoneUtil.use(pLevel, pPos, pPlayer, pHand);
	}

	@Override
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		return canSupportRigidBlock(pLevel, pPos.below());
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}


	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction direction) {
		return true;
	}

	@Override
	public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
		if (!pLevel.isClientSide && (pState.getBlock() != pOldState.getBlock())) {
			RedstoneReceiverWorldSavedData data = RedstoneReceiverWorldSavedData.get((ServerLevel) pLevel);
			data.addNode(pLevel, pPos);
			update((ServerLevel) pLevel, pPos, pState);
		}
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (!pLevel.isClientSide && (pNewState.getBlock() != pState.getBlock())) {
			RedstoneReceiverWorldSavedData.get((ServerLevel) pLevel).removeNode(pLevel, pPos);
			CapabilityUtil.findWirelessCapability(pLevel, pPos, c -> {
				RedstoneReceiverWorldSavedData.get((ServerLevel) pLevel).updateFrequency(pLevel, c.getFrequency());
			});
		}

		super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TransmitterBlockEntity(pPos, pState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(POWER);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
		if (stateIn.getValue(POWER) > 0) {
			double d0 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d1 = (double) pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d2 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			level.addParticle(DustParticleOptions.REDSTONE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
		if (!pLevel.getBlockTicks().hasScheduledTick(pPos, this)) {
			pLevel.scheduleTick(pPos, this, 5);
		}
	}

	@Override
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		update(pLevel, pPos, pState);
	}

	private void update(ServerLevel level, BlockPos pos, BlockState state) {
		int power = findMaxPower(level, pos);
		updateFrequency(level, pos);
		if (power != state.getValue(POWER)) {
			level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(POWER, power));
		}
	}

	public void updateFrequency(ServerLevel level, BlockPos pos, int oldFrequency) {
		RedstoneReceiverWorldSavedData.get(level).updateFrequency(level, oldFrequency);
		updateFrequency(level, pos);
	}

	public void updateFrequency(ServerLevel level, BlockPos pos) {
		int power = findMaxPower(level, pos);
		CapabilityUtil.findWirelessCapability(level, pos, c -> {
			c.setPower(power);
			RedstoneReceiverWorldSavedData.get(level).updateFrequency(level, c.getFrequency());
		});
	}

	private int findMaxPower(Level level, BlockPos pos) {
		int currentMax = 0;

		if (level.getSignal(pos.north(), Direction.NORTH) > currentMax) {
			currentMax = level.getSignal(pos.north(), Direction.NORTH);
		}
		if (level.getSignal(pos.east(), Direction.EAST) > currentMax) {
			currentMax = level.getSignal(pos.east(), Direction.EAST);
		}
		if (level.getSignal(pos.south(), Direction.SOUTH) > currentMax) {
			currentMax = level.getSignal(pos.south(), Direction.SOUTH);
		}
		if (level.getSignal(pos.west(), Direction.WEST) > currentMax) {
			currentMax = level.getSignal(pos.west(), Direction.WEST);
		}

		return currentMax;
	}

	@Override
	public Direction getHighlightFacing(BlockState state) {
		return Direction.NORTH;
	}

	@Override
	public String getHighlightInfo(BlockState state, Direction direction) {
		return I18n.get("info.gates.input");
	}
}
