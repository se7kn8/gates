package com.github.se7_kn8.gates.block.redstone_clock;

import com.github.se7_kn8.gates.tile.RedstoneClockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class RedstoneClock extends RedstoneClockBaseBlock {

	public RedstoneClock() {
		registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(CLOCK_SPEED, 3));
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (player.mayBuild()) {
			if (!worldIn.isClientSide) {
				BlockState newBlockState = state.cycle(CLOCK_SPEED);
				worldIn.setBlockAndUpdate(pos, newBlockState);
				TileEntity entity = worldIn.getBlockEntity(pos);
				if (entity instanceof RedstoneClockTileEntity) {
					RedstoneClockTileEntity clock = (RedstoneClockTileEntity) entity;
					clock.setClockTime(newBlockState.getValue(CLOCK_SPEED) * 4);
					clock.setClockLength(newBlockState.getValue(CLOCK_SPEED) * 2);
					clock.resetClock();
				}
			}
			return ActionResultType.SUCCESS;
		}
		return super.use(state, worldIn, pos, player, handIn, hit);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(POWERED, CLOCK_SPEED);
	}
}
