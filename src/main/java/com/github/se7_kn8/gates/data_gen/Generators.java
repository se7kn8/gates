package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.Gates;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Gates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Generators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		generator.addProvider(new BlockStates(generator, event.getExistingFileHelper()));
		LootGenerator lootGenerator = new LootGenerator(generator);
		LootTablesGenerator.register(lootGenerator);
		generator.addProvider(lootGenerator);
		generator.addProvider(new Recipes(generator));
		generator.addProvider(new BlockModels(generator, event.getExistingFileHelper()));
		generator.addProvider(new ItemModels(generator, event.getExistingFileHelper()));
	}

}
