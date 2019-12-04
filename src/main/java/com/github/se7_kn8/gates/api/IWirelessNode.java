package com.github.se7_kn8.gates.api;

public interface IWirelessNode {

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
