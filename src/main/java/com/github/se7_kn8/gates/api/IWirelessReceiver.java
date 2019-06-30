package com.github.se7_kn8.gates.api;

/**
 * Must be implemented on a {@link net.minecraft.tileentity.TileEntity}
 */
public interface IWirelessReceiver {

	void onReceive(int power);

	int getFrequency();

}
