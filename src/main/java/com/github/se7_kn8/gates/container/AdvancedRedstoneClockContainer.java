package com.github.se7_kn8.gates.container;

import com.github.se7_kn8.gates.GatesContainers;
import com.github.se7_kn8.gates.tile.RedstoneClockTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AdvancedRedstoneClockContainer extends BasicPlayerContainer {
	public AdvancedRedstoneClockContainer(int windowId, World world, BlockPos pos, PlayerInventory inventory, PlayerEntity player) {
		super(GatesContainers.ADVANCED_REDSTONE_CLOCK_CONTAINER_TYPE, windowId, world, pos, inventory, player);
		trackInt(new IntReferenceHolder() {
			@Override
			public int get() {
				return getClockTime();
			}

			@Override
			public void set(int value) {
				setClockTime(value);
			}
		});
		trackInt(new IntReferenceHolder() {
			@Override
			public int get() {
				return getClockLength();
			}

			@Override
			public void set(int value) {
				setClockLength(value);
			}
		});
	}

	public int getClockTime() {
		if (getTile() instanceof RedstoneClockTileEntity) {
			RedstoneClockTileEntity tile = (RedstoneClockTileEntity) getTile();
			return tile.getClockTime();
		}
		return 0;
	}

	public void setClockTime(int clockTime) {
		if (getTile() instanceof RedstoneClockTileEntity) {
			RedstoneClockTileEntity tile = (RedstoneClockTileEntity) getTile();
			tile.setClockTime(clockTime);
		}
	}

	public int getClockLength() {
		if (getTile() instanceof RedstoneClockTileEntity) {
			RedstoneClockTileEntity tile = (RedstoneClockTileEntity) getTile();
			return tile.getClockLength();
		}
		return 0;
	}

	public void setClockLength(int clockLength) {
		if (getTile() instanceof RedstoneClockTileEntity) {
			RedstoneClockTileEntity tile = (RedstoneClockTileEntity) getTile();
			tile.setClockLength(clockLength);
		}
	}

}
