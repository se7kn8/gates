package com.github.se7_kn8.gates.block;

import com.github.se7_kn8.gates.api.IHighlightInfoBlock;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import javax.annotation.Nullable;

public class RSFlipFlop extends DiodeBlock implements IHighlightInfoBlock {

	public RSFlipFlop() {
		super(Properties.copy(Blocks.REPEATER));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, POWERED);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 0;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
		return side == state.getValue(FACING) || side == state.getValue(FACING).getClockWise() || side == state.getValue(FACING).getCounterClockWise();
	}

	@Override
	protected int getInputSignal(Level pLevel, BlockPos pPos, BlockState pState) {
		Direction facing = pState.getValue(FACING);
		boolean set = getAlternateSignalAt(pLevel, pPos.relative(facing.getCounterClockWise()), facing.getCounterClockWise()) > 0;
		boolean reset = getAlternateSignalAt(pLevel, pPos.relative(facing.getClockWise()), facing.getClockWise()) > 0;

		if (set && reset) {
			return 0;
		}

		if (set && !pState.getValue(POWERED)) {
			//world.setBlockState(pos, state.with(SET, true));
			return 15;
		}

		if (reset && pState.getValue(POWERED)) {
			//world.setBlockState(pos, state.with(SET, false));
			return 0;
		}

		return pState.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public Direction getHighlightFacing(BlockState state) {
		return state.getValue(FACING);
	}

	@Override
	public String getHighlightInfo(BlockState state, Direction direction) {
		return switch (direction) {
			case NORTH -> I18n.get("info.gates.output");
			case WEST -> I18n.get("info.gates.reset");
			case EAST -> I18n.get("info.gates.set");
			default -> "";
		};
	}
}
