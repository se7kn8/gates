package com.github.se7_kn8.gates;

import net.minecraftforge.common.ForgeConfigSpec;

public class GatesConfig {

	public final ForgeConfigSpec defaultConfig;

	public final ForgeConfigSpec.IntValue minClockTicks;
	public final ForgeConfigSpec.IntValue maxClockTicks;

	public final ForgeConfigSpec.IntValue minClockPulseTicks;
	public final ForgeConfigSpec.IntValue maxClockPulseTicks;

	GatesConfig() {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		minClockTicks = builder.comment("Min ticks for one clock cycle").defineInRange("min_clock_ticks", 2, 2, Integer.MAX_VALUE);
		maxClockTicks = builder.comment("Max ticks for one clock cycle. Needs to be higher than min_clock_ticks").defineInRange("max_clock_ticks", 72000, 2, Integer.MAX_VALUE);

		minClockPulseTicks = builder.comment("Min ticks for one clock pulse").defineInRange("min_clock_pulse_ticks", 1, 1, Integer.MAX_VALUE);
		maxClockPulseTicks = builder.comment("Max ticks for one clock pulse. Needs to be higher than min_clock_pulse_ticks").defineInRange("max_clock_pluse_ticks", 71000, 2, Integer.MAX_VALUE);

		defaultConfig = builder.build();
	}

}

