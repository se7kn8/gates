package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.Gates;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LootGenerator implements IDataProvider {

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
	public void act(DirectoryCache cache) throws IOException {
		Path outputFolder = this.dataGenerator.getOutputFolder();

		ValidationTracker validator = new ValidationTracker(
				LootParameterSets.GENERIC,
				resourceLocation -> null,
				tables::get
		);
		tables.forEach((resourceLocation, lootTable) -> {
			LootTableManager.validateLootTable(validator, resourceLocation, lootTable);
		});
		Multimap<String, String> multimap = validator.getProblems();
		if (!multimap.isEmpty()) {
			multimap.forEach((id, value) -> {
				Gates.LOGGER.warn("Found validation problem in " + id + ": " + value);
			});
			throw new IllegalStateException("Failed to validate loot tables, see logs");
		} else {
			tables.forEach((resourceLocation, lootTable) -> {
				Path path1 = getPath(outputFolder, resourceLocation);

				try {
					IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path1);
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
