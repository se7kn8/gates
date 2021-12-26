package com.github.se7_kn8.gates.block.redstone_clock;

import com.github.se7_kn8.gates.block.entity.RedstoneClockBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class RedstoneClock extends RedstoneClockBaseBlock {

	public RedstoneClock() {
		registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(CLOCK_SPEED, 3));
	}


	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pPlayer.mayBuild()) {
			if (!pLevel.isClientSide) {
				BlockState newState = pState.cycle(CLOCK_SPEED);
				pLevel.setBlockAndUpdate(pPos, newState);
				BlockEntity entity = pLevel.getBlockEntity(pPos);
				if (entity instanceof RedstoneClockBlockEntity clock) {
					clock.setClockTime(newState.getValue(CLOCK_SPEED) * 4);
					clock.setClockLength(newState.getValue(CLOCK_SPEED) * 2);
					clock.resetClock();
				}

				return InteractionResult.CONSUME;
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(POWERED, CLOCK_SPEED);
	}
}
