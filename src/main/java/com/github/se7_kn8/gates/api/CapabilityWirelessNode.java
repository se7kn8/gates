package com.github.se7_kn8.gates.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class CapabilityWirelessNode {

	public static class WirelessNodeImpl implements IWirelessNode, INBTSerializable<CompoundNBT> {

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
		public CompoundNBT serializeNBT() {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putInt("frequency", frequency);
			nbt.putInt("power", power);
			nbt.putString("type", type.name());
			return nbt;
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt) {
			frequency = nbt.getInt("frequency");
			power = nbt.getInt("power");
			String typeString = nbt.getString("type");
			if (!typeString.equals("")) {
				type = Types.valueOf(typeString);
			} else {
				type= Types.NOOP;
			}
		}
	}

	@CapabilityInject(IWirelessNode.class)
	public static Capability<IWirelessNode> WIRELESS_NODE = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IWirelessNode.class, new Capability.IStorage<IWirelessNode>() {
			@Nullable
			@Override
			public INBT writeNBT(Capability<IWirelessNode> capability, IWirelessNode instance, Direction side) {
				return ((WirelessNodeImpl) instance).serializeNBT();
			}

			@Override
			public void readNBT(Capability<IWirelessNode> capability, IWirelessNode instance, Direction side, INBT nbt) {
				((WirelessNodeImpl) instance).deserializeNBT((CompoundNBT) nbt);
			}
		}, () -> new WirelessNodeImpl(0, IWirelessNode.Types.NOOP));
	}

}
