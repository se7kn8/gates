package com.github.se7_kn8.gates.block.entity;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.block.redstone_clock.RedstoneClock;
import com.github.se7_kn8.gates.menu.AdvancedRedstoneClockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;


public class RedstoneClockBlockEntity extends BlockEntity implements MenuProvider {

	private static final int MIN_CLOCK_TIME;
	private static final int MAX_CLOCK_TIME;
	private static final int MIN_PULSE_LENGTH;
	private static final int MAX_PULSE_LENGTH;

	static {
		int minClockCycleTicks = Gates.config.minClockTicks.get();
		int maxClockCycleTicks = Gates.config.maxClockTicks.get();
		int minClockPulseTicks = Gates.config.minClockPulseTicks.get();
		int maxClockPulseTicks = Gates.config.maxClockPulseTicks.get();

		if (minClockCycleTicks > maxClockCycleTicks) {
			minClockCycleTicks = maxClockCycleTicks - 1;
		}

		if (minClockPulseTicks > maxClockPulseTicks) {
			minClockCycleTicks = maxClockPulseTicks - 1;
		}

		MIN_CLOCK_TIME = minClockCycleTicks;
		MAX_CLOCK_TIME = maxClockCycleTicks;

		MIN_PULSE_LENGTH = minClockPulseTicks;
		MAX_PULSE_LENGTH = maxClockPulseTicks;

	}

	public static final int CLOCK_LENGTH_INDEX = 0;
	public static final int CLOCK_TIME_INDEX = 1;

	private final ContainerData dataAccess = new ContainerData() {
		@Override
		public int get(int pIndex) {
			return switch (pIndex) {
				case CLOCK_LENGTH_INDEX -> clockLength;
				case CLOCK_TIME_INDEX -> clockTime;
				default -> 0;
			};
		}

		@Override
		public void set(int pIndex, int pValue) {
			switch (pIndex) {
				case CLOCK_LENGTH_INDEX -> clockLength = pValue;
				case CLOCK_TIME_INDEX -> clockTime = pValue;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	};

	private int clockLength = 6;
	private int clockTime = 12;

	public RedstoneClockBlockEntity(BlockPos pos, BlockState state) {
		super(GatesBlocks.REDSTONE_CLOCK_BLOCK_ENTITY_TYPE.get(), pos, state);
	}

	private int remainingTicks = 0;
	private int poweredTicks = 0;

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		remainingTicks = pTag.getInt("remaining");
		poweredTicks = pTag.getInt("powered");
		clockLength = pTag.getInt("clockLength");
		clockTime = pTag.getInt("clockTime");
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		pTag.putInt("remaining", remainingTicks);
		pTag.putInt("powered", poweredTicks);
		pTag.putInt("clockLength", clockLength);
		pTag.putInt("clockTime", clockTime);
		super.saveAdditional(pTag);
	}

	public static void clockTick(Level pLevel, BlockPos pPos, BlockState pState, RedstoneClockBlockEntity pBlockEntity) {
		if (pBlockEntity.remainingTicks <= 0) {
			pLevel.setBlockAndUpdate(pPos, pState.setValue(RedstoneClock.POWERED, true));
			pBlockEntity.poweredTicks = pBlockEntity.clockLength;

			pBlockEntity.remainingTicks = pBlockEntity.clockTime;
		} else {
			pBlockEntity.remainingTicks--;
		}
		if (pBlockEntity.poweredTicks <= 0) {
			if (pState.getValue(RedstoneClock.POWERED)) {
				pLevel.setBlockAndUpdate(pPos, pState.setValue(RedstoneClock.POWERED, false));
			}
		} else {
			pBlockEntity.poweredTicks--;
		}
	}

	public int getClockLength() {
		return clockLength;
	}

	public int getClockTime() {
		return clockTime;
	}

	public void setClockLength(int clockLength) {
		if (clockLength < MIN_PULSE_LENGTH) {
			clockLength = MIN_PULSE_LENGTH;
		} else if (clockLength > MAX_PULSE_LENGTH) {
			clockLength = MAX_PULSE_LENGTH;
		}
		this.clockLength = clockLength;
		checkLengthTimeRelation();
	}

	public void setClockTime(int clockTime) {
		if (clockTime < MIN_CLOCK_TIME) {
			clockTime = MIN_CLOCK_TIME;
		} else if (clockTime > MAX_CLOCK_TIME) {
			clockTime = MAX_CLOCK_TIME;
		}
		this.clockTime = clockTime;
		checkLengthTimeRelation();
	}

	public void resetClock() {
		poweredTicks = 0;
		remainingTicks = 0;
	}

	private void checkLengthTimeRelation() {
		if (clockLength >= clockTime) {
			clockLength = clockTime - 1;
		}
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new AdvancedRedstoneClockMenu(i, inventory, this.dataAccess);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.gates.advanced_redstone_clock");
	}
}
