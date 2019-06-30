package com.github.se7_kn8.gates.data;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.api.IWirelessReceiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.*;

public class RedstoneReceiverWorldSavedData extends WorldSavedData {

	public static final String DATA_NAME = Gates.MODID + "_RedstoneReceiver";

	public RedstoneReceiverWorldSavedData() {
		super(RedstoneReceiverWorldSavedData.DATA_NAME);
	}

	public RedstoneReceiverWorldSavedData(String s) {
		super(s);
	}

	private List<BlockPos> receivers = new ArrayList<>();
	private Set<Integer> activeFrequencies = new HashSet<>();

	@Override
	public void read(NBTTagCompound nbt) {
		receivers = new Gson().fromJson(nbt.getString("receivers"), new TypeToken<List<BlockPos>>() {
		}.getType());
		activeFrequencies = new Gson().fromJson(nbt.getString("active_frequencies"), new TypeToken<Set<Integer>>() {
		}.getType());
	}

	@Override
	@Nonnull
	public NBTTagCompound write(@Nonnull NBTTagCompound compound) {
		compound.putString("receivers", new Gson().toJson(receivers));
		compound.putString("active_frequencies", new Gson().toJson(activeFrequencies));
		return compound;
	}

	public List<IWirelessReceiver> getReceivers(World world, int frequency) {
		List<IWirelessReceiver> wirelessReceivers = new ArrayList<>();

		receivers.forEach(pos -> {
			TileEntity e = world.getTileEntity(pos);
			if (e instanceof IWirelessReceiver) {
				IWirelessReceiver receiver = (IWirelessReceiver) e;
				if (receiver.getFrequency() == frequency) {
					wirelessReceivers.add(receiver);
				}
			}
		});

		return wirelessReceivers;
	}

	public void setFrequency(World world, int frequency, int power) {
		getReceivers(world, frequency).forEach(receiver -> receiver.onReceive(power));
	}

	public void addReceiver(BlockPos pos) {
		receivers.add(pos);
	}

	public void removeReceiver(BlockPos pos){
		receivers.remove(pos);
	}

	@Nonnull
	public static RedstoneReceiverWorldSavedData get(World world) {
		RedstoneReceiverWorldSavedData instance = world.getSavedData(world.getDimension().getType(), RedstoneReceiverWorldSavedData::new, DATA_NAME);

		if (instance == null) {
			instance = new RedstoneReceiverWorldSavedData();
			world.setSavedData(world.getDimension().getType(), DATA_NAME, instance);
		}

		return instance;
	}
}