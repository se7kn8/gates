package com.github.se7_kn8.gates.block.wireless_redstone;

import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import com.github.se7_kn8.gates.tile.ReceiverTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ReceiverBlock extends BlockContainer {

	public ReceiverBlock() {
		super(Properties.from(Blocks.REPEATER));
	}

	@Override
	public void onBlockAdded(IBlockState state, World world, BlockPos pos, IBlockState p_196259_4_) {
		super.onBlockAdded(state, world, pos, p_196259_4_);
		RedstoneReceiverWorldSavedData.get(world).addReceiver(pos);
	}

	@Override
	public void onReplaced(IBlockState oldState, World world, BlockPos pos, IBlockState newState, boolean p_196243_5_) {
		super.onReplaced(oldState, world, pos, newState, p_196243_5_);
		if (oldState.getBlock() != newState.getBlock()) {
			RedstoneReceiverWorldSavedData.get(world).removeReceiver(pos);
		}
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new ReceiverTileEntity();
	}
}
