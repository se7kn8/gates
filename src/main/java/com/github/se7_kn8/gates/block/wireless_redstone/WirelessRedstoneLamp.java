package com.github.se7_kn8.gates.block.wireless_redstone;

import com.github.se7_kn8.gates.tile.ReceiverTileEntity;
import com.github.se7_kn8.gates.util.WirelessRedstoneUtil;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
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
import org.apache.http.impl.conn.Wire;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class WirelessRedstoneLamp extends ContainerBlock implements ReceiverTileEntity.IWirelessReceiver {

	public static BooleanProperty LIT = BlockStateProperties.LIT;

	public WirelessRedstoneLamp() {
		super(Properties.copy(Blocks.REDSTONE_LAMP));
		this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LIT);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		return WirelessRedstoneUtil.onBlockActivated(worldIn, pos, player, handIn);
	}

	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		WirelessRedstoneUtil.onBlockAdded(state, worldIn, pos, oldState);
		super.onPlace(state, worldIn, pos, oldState, isMoving);
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		WirelessRedstoneUtil.onReplace(state, worldIn, pos, newState);
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void onPowerChange(World world, BlockPos pos, int newPower) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(LIT, newPower > 0));
	}

	@Nullable
	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		ReceiverTileEntity entity = new ReceiverTileEntity();
		entity.setTranslationKey("gates.block.wireless_redstone_lamp");
		return entity;
	}
}
