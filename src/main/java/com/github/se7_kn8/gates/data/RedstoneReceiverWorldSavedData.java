package com.github.se7_kn8.gates.data;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.api.IWirelessNode;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.*;

public class RedstoneReceiverWorldSavedData extends WorldSavedData {

	public static final String DATA_NAME = Gates.MODID + "_wireless_network";

	public RedstoneReceiverWorldSavedData() {
		super(RedstoneReceiverWorldSavedData.DATA_NAME);
	}

	public RedstoneReceiverWorldSavedData(String s) {
		super(s);
	}

	private Set<BlockPos> receivers = new HashSet<>();
	private Set<BlockPos> transmitters = new HashSet<>();

	@Override
	public void read(CompoundNBT nbt) {
		receivers = loadBlockPosSet(nbt, "receivers");
		transmitters = loadBlockPosSet(nbt, "transmitters");
	}

	public Set<BlockPos> loadBlockPosSet(CompoundNBT compound, String name) {
		ListNBT list = compound.getList(name, 10/*NBT-ID for CompoundNBT*/);
		Set<BlockPos> pos = new HashSet<>();

		for (int i = 0; i < list.size(); i++) {
			CompoundNBT entry = (CompoundNBT) list.get(i);

			int x = entry.getInt("x");
			int y = entry.getInt("y");
			int z = entry.getInt("z");

			pos.add(new BlockPos(x, y, z));
		}

		return pos;
	}

	@Override
	@Nonnull
	public CompoundNBT write(@Nonnull CompoundNBT compound) {
		saveBlockPosSet(receivers, compound, "receivers");
		saveBlockPosSet(transmitters, compound, "transmitters");
		return compound;
	}

	public void saveBlockPosSet(Set<BlockPos> set, CompoundNBT compound, String name) {
		ListNBT list = new ListNBT();
		for (BlockPos pos : set) {
			CompoundNBT entry = new CompoundNBT();
			entry.putInt("x", pos.getX());
			entry.putInt("y", pos.getY());
			entry.putInt("z", pos.getZ());
			list.add(entry);
		}
		compound.put(name, list);
	}

	public int getCurrentFrequencyValue(World world, int frequency) {
		return transmitters
				.stream()
				.map(pos -> {
					TileEntity entity = world.getTileEntity(pos);
					IWirelessNode node = entity.getCapability(CapabilityWirelessNode.WIRELESS_NODE).orElseThrow(IllegalStateException::new);
					return node;
				})
				.filter(node -> node.getFrequency() == frequency)
				.map(IWirelessNode::getPower)
				.max(Integer::compareTo)
				.orElse(0);
	}

	public void updateFrequency(World world, int frequency) {
		int power = getCurrentFrequencyValue(world, frequency);

		/* TODO
		 *
		 * this is necessary because the receivers are changing their blockstates which result in adding/removing elements to the set
		 * -> ConcurrentModificationException
		 *
		 */
		Set<BlockPos> receiversCopy = new HashSet<>(receivers);

		receiversCopy.stream()
				.map(pos -> world.getTileEntity(pos).getCapability(CapabilityWirelessNode.WIRELESS_NODE).orElseThrow(IllegalStateException::new))
				.filter(node -> node.getFrequency() == frequency)
				.forEach(node -> node.setPower(power));
	}

	public void addNode(World world, BlockPos pos) {
		world.getTileEntity(pos).getCapability(CapabilityWirelessNode.WIRELESS_NODE).ifPresent(c -> {
			switch (c.getType()) {
				case RECEIVER:
					addReceiver(pos);
					break;
				case TRANSMITTER:
					addTransmitter(pos);
					break;
			}
		});
	}

	/**
	 * Must be called before the tile entity is removed from the world
	 */
	public void removeNode(World world, BlockPos pos) {
		world.getTileEntity(pos).getCapability(CapabilityWirelessNode.WIRELESS_NODE).ifPresent(c -> {
			switch (c.getType()) {
				case RECEIVER:
					removeReceiver(pos);
					break;
				case TRANSMITTER:
					removeTransmitter(pos);
					updateFrequency(world, c.getFrequency());
					break;
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

	@Nonnull
	public static RedstoneReceiverWorldSavedData get(ServerWorld world) {
		return world.getSavedData().getOrCreate(RedstoneReceiverWorldSavedData::new, DATA_NAME);
	}
}