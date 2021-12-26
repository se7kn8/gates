package com.github.se7_kn8.gates.util;

import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import com.github.se7_kn8.gates.item.FrequencyChangerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WirelessRedstoneUtil {

	public static InteractionResult use(Level level, BlockPos pos, Player player, InteractionHand hand) {
		if (player.getItemInHand(hand).getItem() instanceof FrequencyChangerItem && player.getItemInHand(hand).hasTag() && player.getItemInHand(hand).getTag().contains("frequency")) {
			return InteractionResult.PASS;
		}
		if (!level.isClientSide) {
			MenuProvider provider = level.getBlockState(pos).getMenuProvider(level, pos);
			if (provider != null) {
				player.openMenu(provider);
			}
		}
		return InteractionResult.SUCCESS;
	}

	/**
	 * Must be called before super.onBlockAdded
	 */
	public static void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState) {
		if (!level.isClientSide && (state.getBlock() != oldState.getBlock())) {
			RedstoneReceiverWorldSavedData data = RedstoneReceiverWorldSavedData.get((ServerLevel) level);
			data.addNode(level, pos);
		}
	}

	/**
	 * Must be called before super.onReplace
	 */
	public static void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState) {
		if (!level.isClientSide && (newState.getBlock() != state.getBlock())) {
			RedstoneReceiverWorldSavedData.get((ServerLevel) level).removeNode(level, pos);
		}
	}

}
