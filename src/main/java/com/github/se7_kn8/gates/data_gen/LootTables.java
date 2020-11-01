package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.GatesBlocks;
import net.minecraft.block.Block;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.util.ResourceLocation;

public class LootTables {

	public static void register(LootGenerator generator) {
		generateSelfDrop(generator, GatesBlocks.REDSTONE_BLOCK_OFF);
		generateSelfDrop(generator, GatesBlocks.AND_GATE);
		generateSelfDrop(generator, GatesBlocks.OR_GATE);
		generateSelfDrop(generator, GatesBlocks.NAND_GATE);
		generateSelfDrop(generator, GatesBlocks.NOR_GATE);
		generateSelfDrop(generator, GatesBlocks.XOR_GATE);
		generateSelfDrop(generator, GatesBlocks.XNOR_GATE);
		generateSelfDrop(generator, GatesBlocks.NOT_GATE);
		generateSelfDrop(generator, GatesBlocks.FAST_REPEATER);
		generateSelfDrop(generator, GatesBlocks.SLOW_REPEATER);
		generateSelfDrop(generator, GatesBlocks.WIRELESS_REDSTONE_RECEIVER);
		generateSelfDrop(generator, GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER);
		generateSelfDrop(generator, GatesBlocks.WIRELESS_REDSTONE_LAMP);
		generateSelfDrop(generator, GatesBlocks.RAIN_DETECTOR);
		generateSelfDrop(generator, GatesBlocks.THUNDER_DETECTOR);
		generateSelfDrop(generator, GatesBlocks.REDSTONE_CLOCK);
		generateSelfDrop(generator, GatesBlocks.ADVANCED_REDSTONE_CLOCK);
		generateSelfDrop(generator, GatesBlocks.ROTARY_SWITCH);
		generateSelfDrop(generator, GatesBlocks.RS_FLIP_FLOP);
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
