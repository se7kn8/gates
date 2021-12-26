package com.github.se7_kn8.gates.api;

public interface IWirelessNode {

	int FREQUENCY_INDEX = 0;
	int POWER_INDEX = 1;
	int TYPE_INDEX = 2;

	enum Types {
		NOOP,
		TRANSMITTER,
		RECEIVER
	}

	int getFrequency();

	void setFrequency(int newFrequency);

	void setPower(int newPower);

	/**
	 * Will only used when {@link #getType()} returns {@link Types#TRANSMITTER}
	 * */
	int getPower();

	Types getType();

	default int getMaxFrequency() {
		return Short.MAX_VALUE;
	}

	default int getMinFrequency() {
		return 1;
	}
}
