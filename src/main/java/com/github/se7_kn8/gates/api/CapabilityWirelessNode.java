package com.github.se7_kn8.gates.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

public class CapabilityWirelessNode {

	public static class WirelessNodeImpl implements IWirelessNode, INBTSerializable<CompoundTag> {

		public ContainerData dataAccess = new ContainerData() {
			@Override
			public int get(int pIndex) {
				return switch (pIndex) {
					case FREQUENCY_INDEX -> frequency;
					case POWER_INDEX -> power;
					case TYPE_INDEX -> type.ordinal();
					default -> 0;
				};
			}

			@Override
			public void set(int pIndex, int pValue) {
				switch (pIndex) {
					case FREQUENCY_INDEX -> frequency = pValue;
					case POWER_INDEX -> power = pValue;
					case TYPE_INDEX -> type = Types.values()[pValue];
				}
			}

			@Override
			public int getCount() {
				return 3;
			}
		};

		int frequency;
		int power;
		Types type;

		public WirelessNodeImpl(int frequency, Types type) {
			this.frequency = frequency;
			this.type = type;
		}

		@Override
		public void setFrequency(int newFrequency) {
			this.frequency = newFrequency;
		}

		@Override
		public int getFrequency() {
			return frequency;
		}

		@Override
		public void setPower(int newPower) {
			this.power = newPower;
		}

		@Override
		public int getPower() {
			return power;
		}

		@Override
		public Types getType() {
			return type;
		}

		@Override
		public CompoundTag serializeNBT() {
			CompoundTag nbt = new CompoundTag();
			nbt.putInt("frequency", frequency);
			nbt.putInt("power", power);
			nbt.putString("type", type.name());
			return nbt;
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			frequency = nbt.getInt("frequency");
			power = nbt.getInt("power");
			String typeString = nbt.getString("type");
			if (!typeString.equals("")) {
				type = Types.valueOf(typeString);
			} else {
				type = Types.NOOP;
			}
		}


	}

	public static final Capability<IWirelessNode> WIRELESS_NODE = CapabilityManager.get(new CapabilityToken<>() {
	});
}
