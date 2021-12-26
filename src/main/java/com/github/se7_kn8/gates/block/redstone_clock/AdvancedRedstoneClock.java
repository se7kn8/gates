package com.github.se7_kn8.gates.block.redstone_clock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;


public class AdvancedRedstoneClock extends RedstoneClockBaseBlock {

	public AdvancedRedstoneClock() {
		registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pLevel.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			MenuProvider provider = this.getMenuProvider(pState, pLevel, pPos);
			if (provider != null) {
				pPlayer.openMenu(provider);
			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(POWERED);
	}
}
