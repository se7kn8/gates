package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.GatesBlocks;
import net.minecraft.block.Block;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.util.ResourceLocation;

public class LootTables {

	public static void register(LootGenerator generator) {
		generateSelfDrop(generator, GatesBlocks.REDSTONE_BLOCK_OFF.get());
		generateSelfDrop(generator, GatesBlocks.AND_GATE.get());
		generateSelfDrop(generator, GatesBlocks.OR_GATE.get());
		generateSelfDrop(generator, GatesBlocks.NAND_GATE.get());
		generateSelfDrop(generator, GatesBlocks.NOR_GATE.get());
		generateSelfDrop(generator, GatesBlocks.XOR_GATE.get());
		generateSelfDrop(generator, GatesBlocks.XNOR_GATE.get());
		generateSelfDrop(generator, GatesBlocks.NOT_GATE.get());
		generateSelfDrop(generator, GatesBlocks.FAST_REPEATER.get());
		generateSelfDrop(generator, GatesBlocks.SLOW_REPEATER.get());
		generateSelfDrop(generator, GatesBlocks.WIRELESS_REDSTONE_RECEIVER.get());
		generateSelfDrop(generator, GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER.get());
		generateSelfDrop(generator, GatesBlocks.WIRELESS_REDSTONE_LAMP.get());
		generateSelfDrop(generator, GatesBlocks.RAIN_DETECTOR.get());
		generateSelfDrop(generator, GatesBlocks.THUNDER_DETECTOR.get());
		generateSelfDrop(generator, GatesBlocks.REDSTONE_CLOCK.get());
		generateSelfDrop(generator, GatesBlocks.ADVANCED_REDSTONE_CLOCK.get());
		generateSelfDrop(generator, GatesBlocks.ROTARY_SWITCH.get());
		generateSelfDrop(generator, GatesBlocks.RS_FLIP_FLOP.get());
	}

	private static void generateSelfDrop(LootGenerator generator, Block block) {
		ResourceLocation location = block.getLootTable();
		LootTable table = LootTable.builder()
				.setParameterSet(LootParameterSets.BLOCK)
				.addLootPool(LootPool.builder()
						.rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(block))
						.acceptCondition(SurvivesExplosion.builder())
				).build();
		generator.add(location, table);
	}

}
