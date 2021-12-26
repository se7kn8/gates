package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.GatesBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTablesGenerator {

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
		generateSelfDrop(generator, GatesBlocks.WAXED_REDSTONE_WIRE.get());
	}

	private static void generateSelfDrop(LootGenerator generator, Block block) {
		ResourceLocation location = block.getLootTable();
		LootTable table = LootTable.lootTable()
				.setParamSet(LootContextParamSets.BLOCK)
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(block))
						.when(ExplosionCondition.survivesExplosion())
				).build();
		generator.add(location, table);
	}

}
