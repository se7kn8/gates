package com.github.se7_kn8.gates.tile;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.block.RedstoneClock;
import com.github.se7_kn8.gates.container.AdvancedRedstoneClockContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class RedstoneClockTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {


	public static final int MIN_CLOCK_TIME = 2;
	public static final int MAX_CLOCK_TIME = 72000;
	public static final int MIN_CLOCK_LENGTH = 1;
	public static final int MAX_CLOCK_LENGTH = 71000;

	private int clockLength = 6;
	private int clockTime = 12;

	public RedstoneClockTileEntity() {
		super(GatesBlocks.REDSTONE_CLOCK_TILE_ENTITY_TYPE);
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		remainingTicks = compound.getInt("remaining");
		poweredTicks = compound.getInt("powered");
		clockLength = compound.getInt("clockLength");
		clockTime = compound.getInt("clockTime");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putInt("remaining", remainingTicks);
		compound.putInt("powered", poweredTicks);
		compound.putInt("clockLength", clockLength);
		compound.putInt("clockTime", clockTime);
		return super.write(compound);
	}

	private int remainingTicks = 0;
	private int poweredTicks = 0;


	@Override
	public void tick() {
		if (!world.isRemote) {
			if (remainingTicks <= 0) {
				world.setBlockState(pos, world.getBlockState(pos).with(RedstoneClock.POWERED, true));
				poweredTicks = clockLength;

				remainingTicks = clockTime;
			} else {
				remainingTicks--;
			}
			if (poweredTicks <= 0) {
				if (world.getBlockState(pos).get(RedstoneClock.POWERED)) {
					world.setBlockState(pos, world.getBlockState(pos).with(RedstoneClock.POWERED, false));
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
		return new AdvancedRedstoneClockContainer(windowId, world, pos, inventory, player);
	}

	public int getClockLength() {
		return clockLength;
	}

	public int getClockTime() {
		return clockTime;
	}

	public void setClockLength(int clockLength) {
		if (clockLength < MIN_CLOCK_LENGTH) {
			clockLength = MIN_CLOCK_LENGTH;
		} else if (clockLength > MAX_CLOCK_LENGTH) {
			clockLength = MAX_CLOCK_LENGTH;
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

	public void resetClock(){
		poweredTicks = 0;
		remainingTicks = 0;
	}

	private void checkLengthTimeRelation() {
		if (clockLength >= clockTime) {
			clockLength = clockTime - 1;
		}
	}

}
