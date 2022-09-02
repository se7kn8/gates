package com.github.se7_kn8.gates.data_gen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTables extends LootTableProvider {

	public LootTables(DataGenerator pGenerator) {
		super(pGenerator);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
		return ImmutableList.of(
				Pair.of(BlockLoots::new, LootContextParamSets.BLOCK)
		);
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
		map.forEach((key, value)-> net.minecraft.world.level.storage.loot.LootTables.validate(validationtracker, key, value));
	}
}
