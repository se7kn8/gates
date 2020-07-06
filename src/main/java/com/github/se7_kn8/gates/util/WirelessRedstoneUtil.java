package com.github.se7_kn8.gates.util;

import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import com.github.se7_kn8.gates.item.FrequencyChangerItem;
import com.github.se7_kn8.gates.tile.ReceiverTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class WirelessRedstoneUtil {

	public static ActionResultType onBlockActivated(World world, BlockPos pos, PlayerEntity player, Hand hand) {
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

	/**
	 * Must be called before super.onBlockAdded
	 */
	public static void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState) {
		if (!worldIn.isRemote && (state.getBlock() != oldState.getBlock())) {
			RedstoneReceiverWorldSavedData data = RedstoneReceiverWorldSavedData.get((ServerWorld) worldIn);
			data.addNode(worldIn, pos);
		}
	}

	/**
	 * Must be called before super.onReplace
	 */
	public static void onReplace(BlockState state, World worldIn, BlockPos pos, BlockState newState){
		if (!worldIn.isRemote && (newState.getBlock() != state.getBlock())) {
			RedstoneReceiverWorldSavedData.get((ServerWorld) worldIn).removeNode(worldIn, pos);
		}
	}

}
