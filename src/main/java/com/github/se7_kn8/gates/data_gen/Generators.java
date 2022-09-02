package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.Gates;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Gates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Generators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		generator.addProvider(event.includeServer(), new BlockStates(generator, event.getExistingFileHelper()));
		generator.addProvider(event.includeServer(), new BlockTags(generator, event.getExistingFileHelper()));
		generator.addProvider(event.includeServer(), new LootTables(generator));
		generator.addProvider(event.includeServer(), new Recipes(generator));

		generator.addProvider(event.includeClient(), new BlockModels(generator, event.getExistingFileHelper()));
		generator.addProvider(event.includeClient(), new ItemModels(generator, event.getExistingFileHelper()));
	}

}
