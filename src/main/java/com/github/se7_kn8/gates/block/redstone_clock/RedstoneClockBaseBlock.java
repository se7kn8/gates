package com.github.se7_kn8.gates.block.redstone_clock;

import com.github.se7_kn8.gates.tile.RedstoneClockTileEntity;
import com.github.se7_kn8.gates.util.Utils;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class RedstoneClockBaseBlock extends ContainerBlock {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public static final IntegerProperty CLOCK_SPEED = IntegerProperty.create("clock_speed", 1, 5);

	@Nonnull
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
		return Utils.GATE_SHAPE;
	}

	protected RedstoneClockBaseBlock() {
		super(Properties.from(Blocks.REPEATER));
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return true;
	}

	@Override
	public boolean canProvidePower(@Nonnull BlockState p_149744_1_) {
		return true;
	}

	@Override
	public int getStrongPower(BlockState blockState, @Nonnull IBlockReader blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return blockState.get(POWERED) ? 15 : 0;
	}

	@Override
	public int getWeakPower(BlockState blockState, @Nonnull IBlockReader blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return blockState.get(POWERED) ? 15 : 0;
	}

	@Override
	public BlockRenderType getRenderType(@Nonnull BlockState p_149645_1_) {
		return BlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
		return new RedstoneClockTileEntity();
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.get(POWERED)) {
			double d0 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d1 = (double) pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d2 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

}
