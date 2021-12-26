package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.Gates;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LootGenerator implements DataProvider {

	private final DataGenerator dataGenerator;
	private final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private final Map<ResourceLocation, LootTable> tables = new HashMap<>();

	public LootGenerator(DataGenerator dataGenerator) {
		this.dataGenerator = dataGenerator;
	}

	public void add(ResourceLocation location, LootTable table) {
		tables.put(location, table);
	}

	@Override
	public void run(HashCache cache) {
		Path outputFolder = this.dataGenerator.getOutputFolder();

		ValidationContext validator = new ValidationContext(
				LootContextParamSets.ALL_PARAMS,
				resourceLocation -> null,
				tables::get
		);

		tables.forEach((resourceLocation, lootTable) -> {
			LootTables.validate(validator, resourceLocation, lootTable);
		});
		Multimap<String, String> multimap = validator.getProblems();
		if (!multimap.isEmpty()) {
			multimap.forEach((id, value) -> Gates.LOGGER.warn("Found validation problem in " + id + ": " + value));
			throw new IllegalStateException("Failed to validate loot tables, see logs");
		} else {
			tables.forEach((resourceLocation, lootTable) -> {
				Path path1 = getPath(outputFolder, resourceLocation);

				try {
					DataProvider.save(GSON, cache, LootTables.serialize(lootTable), path1);
				} catch (IOException var6) {
					Gates.LOGGER.error("Couldn't save loot table {}", path1, var6);
				}
			});
		}
	}

	private static Path getPath(Path pathIn, ResourceLocation id) {
		return pathIn.resolve("data/" + id.getNamespace() + "/loot_tables/" + id.getPath() + ".json");
	}

	@Override
	public String getName() {
		return "GatesLootTable";
	}
}
