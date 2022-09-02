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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockLoots implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

	private BiConsumer<ResourceLocation, LootTable.Builder> generator;

	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> generator) {
		this.generator = generator;
		generateSelfDrop(GatesBlocks.REDSTONE_BLOCK_OFF.get());
		generateSelfDrop(GatesBlocks.AND_GATE.get());
		generateSelfDrop(GatesBlocks.OR_GATE.get());
		generateSelfDrop(GatesBlocks.NAND_GATE.get());
		generateSelfDrop(GatesBlocks.NOR_GATE.get());
		generateSelfDrop(GatesBlocks.XOR_GATE.get());
		generateSelfDrop(GatesBlocks.XNOR_GATE.get());
		generateSelfDrop(GatesBlocks.NOT_GATE.get());
		generateSelfDrop(GatesBlocks.FAST_REPEATER.get());
		generateSelfDrop(GatesBlocks.SLOW_REPEATER.get());
		generateSelfDrop(GatesBlocks.WIRELESS_REDSTONE_RECEIVER.get());
		generateSelfDrop(GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER.get());
		generateSelfDrop(GatesBlocks.WIRELESS_REDSTONE_LAMP.get());
		generateSelfDrop(GatesBlocks.RAIN_DETECTOR.get());
		generateSelfDrop(GatesBlocks.THUNDER_DETECTOR.get());
		generateSelfDrop(GatesBlocks.REDSTONE_CLOCK.get());
		generateSelfDrop(GatesBlocks.ADVANCED_REDSTONE_CLOCK.get());
		generateSelfDrop(GatesBlocks.ROTARY_SWITCH.get());
		generateSelfDrop(GatesBlocks.RS_FLIP_FLOP.get());
		generateSelfDrop(GatesBlocks.WAXED_REDSTONE_WIRE.get());
	}

	private void generateSelfDrop(Block block) {
		ResourceLocation location = block.getLootTable();
		LootTable.Builder table = LootTable.lootTable()
				.setParamSet(LootContextParamSets.BLOCK)
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(block))
						.when(ExplosionCondition.survivesExplosion())
				);
		generator.accept(location, table);
	}
}
