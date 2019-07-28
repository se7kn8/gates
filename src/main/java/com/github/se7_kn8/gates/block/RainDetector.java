package com.github.se7_kn8.gates.block;

import com.github.se7_kn8.gates.tile.RainDetectorTile;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RainDetector extends ContainerBlock {

	public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;
	public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
	public static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);

	public RainDetector() {
		super(Properties.from(Blocks.DAYLIGHT_DETECTOR));
		this.setDefaultState(this.stateContainer.getBaseState().with(INVERTED, false).with(POWER, 0));
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPE;
	}

	@Override
	public boolean func_220074_n(BlockState p_220074_1_) {
		return true;
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader p_180656_2_, BlockPos p_180656_3_, Direction p_180656_4_) {
		return state.get(POWER);
	}

	public static void updatePower(BlockState state, World world, BlockPos pos) {
		int newPower = state.get(INVERTED) ? (!world.isRainingAt(pos.up()) ? 15 : 0) : (world.isRainingAt(pos.up()) ? 15 : 0);

		if (newPower != state.get(POWER)) {
			world.setBlockState(pos, state.with(POWER, newPower));
		}

	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand p_220051_5_, BlockRayTraceResult p_220051_6_) {
		if (player.isAllowEdit()) {
			if (world.isRemote()) {
				return true;
			} else {
				BlockState newState = state.cycle(INVERTED);
				world.setBlockState(pos, newState, 4);
				updatePower(newState, world, pos);
				return true;
			}
		}
		return false;
	}

	@Override
	public BlockRenderType getRenderType(BlockState p_149645_1_) {
		return BlockRenderType.MODEL;
	}

	@Override
	public boolean canProvidePower(BlockState p_149744_1_) {
		return true;
	}


	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(POWER, INVERTED);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new RainDetectorTile();
	}
}
