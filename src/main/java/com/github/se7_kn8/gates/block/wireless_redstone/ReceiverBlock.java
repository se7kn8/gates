package com.github.se7_kn8.gates.block.wireless_redstone;

import com.github.se7_kn8.gates.api.CapabilityUtil;
import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import com.github.se7_kn8.gates.item.FrequencyChangerItem;
import com.github.se7_kn8.gates.tile.ReceiverTileEntity;
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

public class ReceiverBlock extends ContainerBlock {

	public static final VoxelShape SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 10.0D, 9.0D));

	public static IntegerProperty POWER = BlockStateProperties.POWER_0_15;

	public ReceiverBlock() {
		super(Properties.from(Blocks.REPEATER));
		this.setDefaultState(this.stateContainer.getBaseState().with(POWER, 0));
	}

	public ReceiverBlock(Properties properties){
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(POWER, 0));
	}


	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (player.getHeldItem(hand).getItem() instanceof FrequencyChangerItem && player.getHeldItem(hand).hasTag() && player.getHeldItem(hand).getTag().contains("frequency")) {
			return ActionResultType.PASS;
		}
		if (!world.isRemote) {
			TileEntity entity = world.getTileEntity(pos);
			if (entity instanceof ReceiverTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) entity, entity.getPos());
			}
		}
		return ActionResultType.SUCCESS;
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return hasSolidSideOnTop(worldIn, pos.down());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
/*TODO
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
*/
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
		if (!worldIn.isRemote) {
			RedstoneReceiverWorldSavedData data = RedstoneReceiverWorldSavedData.get((ServerWorld) worldIn);
			data.addNode(worldIn, pos);

			CapabilityUtil.findWirelessCapability(worldIn, pos, c->{
				int value = data.getCurrentFrequencyValue(worldIn, c.getFrequency());
				if (state.get(POWER) != value) {
					worldIn.setBlockState(pos, state.with(POWER, value));
				}
			});
		}
	}


	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!worldIn.isRemote) {
			RedstoneReceiverWorldSavedData.get((ServerWorld) worldIn).removeNode(worldIn, pos);
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new ReceiverTileEntity();
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return true;
	}

	@Override
	public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return blockState.get(POWER);
	}

	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return blockState.get(POWER);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(POWER);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.get(POWER) > 0) {
			double d0 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d1 = (double) pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d2 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

}
