package com.github.se7_kn8.gates.data;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.api.CapabilityUtil;
import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.api.IWirelessNode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RedstoneReceiverWorldSavedData extends SavedData {

	public static final String DATA_NAME = Gates.MODID + "_wireless_network";

	private final Set<BlockPos> receivers;
	private final Set<BlockPos> transmitters;

	public RedstoneReceiverWorldSavedData(CompoundTag compound) {
		receivers = loadBlockPosSet(compound, "receivers");
		transmitters = loadBlockPosSet(compound, "transmitters");
	}

	public RedstoneReceiverWorldSavedData() {
		receivers = new HashSet<>();
		transmitters = new HashSet<>();
	}

	public Set<BlockPos> loadBlockPosSet(CompoundTag compound, String name) {
		ListTag list = compound.getList(name, 10/*NBT-ID for CompoundNBT*/);
		Set<BlockPos> pos = new HashSet<>();

		for (Tag tag : list) {
			CompoundTag entry = (CompoundTag) tag;

			int x = entry.getInt("x");
			int y = entry.getInt("y");
			int z = entry.getInt("z");

			pos.add(new BlockPos(x, y, z));
		}

		return pos;
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		saveBlockPosSet(receivers, compound, "receivers");
		saveBlockPosSet(transmitters, compound, "transmitters");
		return compound;
	}

	public void saveBlockPosSet(Set<BlockPos> set, CompoundTag compound, String name) {
		ListTag list = new ListTag();
		for (BlockPos pos : set) {
			CompoundTag entry = new CompoundTag();
			entry.putInt("x", pos.getX());
			entry.putInt("y", pos.getY());
			entry.putInt("z", pos.getZ());
			list.add(entry);
		}
		compound.put(name, list);
	}

	public int getCurrentFrequencyValue(Level level, int frequency) {
		return transmitters
				.stream()
				.filter(Objects::nonNull)
				.map(pos -> level.getBlockEntity(pos).getCapability(CapabilityWirelessNode.WIRELESS_NODE).orElseGet(() -> new CapabilityWirelessNode.WirelessNodeImpl(-1, IWirelessNode.Types.INVALID)))
				.filter(node -> node.getFrequency() == frequency)
				.map(IWirelessNode::getPower)
				.max(Integer::compareTo)
				.orElse(0);
	}

	public void updateFrequency(Level level, int frequency) {
		int power = getCurrentFrequencyValue(level, frequency);
		updateFrequency(level, frequency, power);
	}

	public void updateFrequency(Level level, int frequency, int power) {
		/* TODO
		 *
		 * this is necessary because the receivers are changing their blockstates which result in adding/removing elements to the set
		 * -> ConcurrentModificationException
		 *
		 */
		Set<BlockPos> receiversCopy = new HashSet<>(receivers);

		receiversCopy.stream()
				.map(level::getBlockEntity)
				.filter(Objects::nonNull)
				.map(blockEntity -> blockEntity.getCapability(CapabilityWirelessNode.WIRELESS_NODE).orElseGet(() -> new CapabilityWirelessNode.WirelessNodeImpl(-1, IWirelessNode.Types.NOOP)))
				.filter(node -> node.getFrequency() == frequency)
				.forEach(node -> node.setPower(power));
	}

	public void addNode(Level level, BlockPos pos) {
		CapabilityUtil.findWirelessCapability(level, pos, c -> {
			switch (c.getType()) {
				case RECEIVER -> {
					c.setPower(this.getCurrentFrequencyValue(level, c.getFrequency()));
					addReceiver(pos);
				}
				case TRANSMITTER -> addTransmitter(pos);
			}
		});
	}

	/**
	 * Must be called before the tile entity is removed from the level
	 */
	public void removeNode(Level level, BlockPos pos) {
		CapabilityUtil.findWirelessCapability(level, pos, c -> {
			switch (c.getType()) {
				case RECEIVER -> removeReceiver(pos);
				case TRANSMITTER -> removeTransmitter(pos);
			}
		});
	}

	private void addReceiver(BlockPos pos) {
		if (receivers.add(pos)) {
			this.setDirty(true);
		}
	}

	private void removeReceiver(BlockPos pos) {
		if (receivers.remove(pos)) {
			this.setDirty(true);
		}
	}

	private void addTransmitter(BlockPos pos) {
		if (transmitters.add(pos)) {
			this.setDirty(true);
		}
	}

	private void removeTransmitter(BlockPos pos) {
		if (transmitters.remove(pos)) {
			this.setDirty(true);
		}
	}

	public static RedstoneReceiverWorldSavedData get(ServerLevel level) {
		return level.getDataStorage().computeIfAbsent(RedstoneReceiverWorldSavedData::new, RedstoneReceiverWorldSavedData::new, DATA_NAME);
	}
}
