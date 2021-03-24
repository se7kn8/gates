package com.github.se7_kn8.gates.block.wireless_redstone;

import com.github.se7_kn8.gates.api.CapabilityUtil;
import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import com.github.se7_kn8.gates.item.FrequencyChangerItem;
import com.github.se7_kn8.gates.tile.TransmitterTileEntity;
import com.github.se7_kn8.gates.util.Utils;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class TransmitterBlock extends ContainerBlock {

	public static final VoxelShape SHAPE = VoxelShapes.or(Utils.GATE_SHAPE, Block.box(7.0D, 0.0D, 7.0D, 9.0D, 10.0D, 9.0D));

	public static IntegerProperty POWER = BlockStateProperties.POWER;

	public TransmitterBlock() {
		super(Properties.copy(Blocks.REPEATER));

		this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0));
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (player.getItemInHand(handIn).getItem() instanceof FrequencyChangerItem && player.getItemInHand(handIn).hasTag() && player.getItemInHand(handIn).getTag().contains("frequency")) {
			return ActionResultType.PASS;
		}
		if (!worldIn.isClientSide) {
			TileEntity entity = worldIn.getBlockEntity(pos);
			if (entity instanceof TransmitterTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) entity, entity.getBlockPos());
			}
		}
		return ActionResultType.SUCCESS;
	}

	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return canSupportRigidBlock(worldIn, pos.below());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		if (!worldIn.isClientSide && (state.getBlock() != oldState.getBlock())) {
			RedstoneReceiverWorldSavedData data = RedstoneReceiverWorldSavedData.get((ServerWorld) worldIn);
			data.addNode(worldIn, pos);
			update(worldIn, pos, state);
		}
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!worldIn.isClientSide && (newState.getBlock() != state.getBlock())) {
			RedstoneReceiverWorldSavedData.get((ServerWorld) worldIn).removeNode(worldIn, pos);
			CapabilityUtil.findWirelessCapability(worldIn, pos, c -> {
				RedstoneReceiverWorldSavedData.get((ServerWorld) worldIn).updateFrequency(worldIn, c.getFrequency());
			});
		}
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

	@Nullable
	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return new TransmitterTileEntity();
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(POWER);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(POWER) > 0) {
			double d0 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d1 = (double) pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d2 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			worldIn.addParticle(RedstoneParticleData.REDSTONE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.getBlockTicks().hasScheduledTick(pos, this)) {
			worldIn.getBlockTicks().scheduleTick(pos, this, 5);
		}
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random p_225534_4_) {
		update(world, pos, state);
	}

	private void update(World worldIn, BlockPos pos, BlockState state) {
		int power = findMaxPower(worldIn, pos);
		updateFrequency((ServerWorld) worldIn, pos);
		if (power != state.getValue(POWER)) {
			worldIn.setBlockAndUpdate(pos, worldIn.getBlockState(pos).setValue(POWER, power));
		}
	}

	public void updateFrequency(ServerWorld world, BlockPos pos, int oldFrequency) {
		RedstoneReceiverWorldSavedData.get(world).updateFrequency(world, oldFrequency);
		updateFrequency(world, pos);
	}

	public void updateFrequency(ServerWorld world, BlockPos pos) {
		int power = findMaxPower(world, pos);
		CapabilityUtil.findWirelessCapability(world, pos, c -> {
			c.setPower(power);
			RedstoneReceiverWorldSavedData.get(world).updateFrequency(world, c.getFrequency());
		});
	}

	private int findMaxPower(World world, BlockPos pos) {
		int currentMax = 0;

		if (world.getSignal(pos.north(), Direction.NORTH) > currentMax) {
			currentMax = world.getSignal(pos.north(), Direction.NORTH);
		}
		if (world.getSignal(pos.east(), Direction.EAST) > currentMax) {
			currentMax = world.getSignal(pos.east(), Direction.EAST);
		}
		if (world.getSignal(pos.south(), Direction.SOUTH) > currentMax) {
			currentMax = world.getSignal(pos.south(), Direction.SOUTH);
		}
		if (world.getSignal(pos.west(), Direction.WEST) > currentMax) {
			currentMax = world.getSignal(pos.west(), Direction.WEST);
		}

		return currentMax;
	}

}
