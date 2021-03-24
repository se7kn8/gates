package com.github.se7_kn8.gates.tile;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.block.redstone_clock.RedstoneClock;
import com.github.se7_kn8.gates.container.AdvancedRedstoneClockContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RedstoneClockTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {


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

	private int clockLength = 6;
	private int clockTime = 12;

	public RedstoneClockTileEntity() {
		super(GatesBlocks.REDSTONE_CLOCK_TILE_ENTITY_TYPE.get());
	}

	@Override
	public void load(@Nonnull BlockState state, @Nonnull CompoundNBT compound) {
		super.load(state, compound);
		remainingTicks = compound.getInt("remaining");
		poweredTicks = compound.getInt("powered");
		clockLength = compound.getInt("clockLength");
		clockTime = compound.getInt("clockTime");
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putInt("remaining", remainingTicks);
		compound.putInt("powered", poweredTicks);
		compound.putInt("clockLength", clockLength);
		compound.putInt("clockTime", clockTime);
		return super.save(compound);
	}

	private int remainingTicks = 0;
	private int poweredTicks = 0;


	@Override
	public void tick() {
		if (!level.isClientSide) {
			if (remainingTicks <= 0) {
				level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(RedstoneClock.POWERED, true));
				poweredTicks = clockLength;

				remainingTicks = clockTime;
			} else {
				remainingTicks--;
			}
			if (poweredTicks <= 0) {
				if (level.getBlockState(worldPosition).getValue(RedstoneClock.POWERED)) {
					level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(RedstoneClock.POWERED, false));
				}
			} else {
				poweredTicks--;
			}
		}
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("block.gates.advanced_redstone_clock");
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
		return new AdvancedRedstoneClockContainer(windowId, level, worldPosition, inventory, player);
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

}
