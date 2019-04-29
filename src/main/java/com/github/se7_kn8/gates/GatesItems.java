package com.github.se7_kn8.gates;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Gates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatesItems {

	public static final Map<ResourceLocation, Item> ITEMS = new HashMap<>();

	@SubscribeEvent
	public static void onBlocksRegistry(RegistryEvent.Register<Item> itemRegistryEvent) {
		for (ResourceLocation location : GatesItems.ITEMS.keySet()) {
			Item item = GatesItems.ITEMS.get(location);
			item.setRegistryName(location);
			itemRegistryEvent.getRegistry().register(item);
		}
	}

}
