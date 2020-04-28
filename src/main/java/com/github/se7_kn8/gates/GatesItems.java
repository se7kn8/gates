package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.item.FrequencyChangerItem;
import com.github.se7_kn8.gates.item.PortableRedstoneTransmitter;
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

	public static final Item REDSTONE_TORCH_PEARL = addItem("redstone_torch_pearl", new Item(new Item.Properties().group(Gates.GATES_ITEM_GROUP)));

	public static final Item FREQUENCY_CHANGER = addItem("frequency_changer", new FrequencyChangerItem());

	public static final Item PORTABLE_REDSTONE_TRANSMITTER = addItem("portable_redstone_transmitter", new PortableRedstoneTransmitter());

	@SubscribeEvent
	public static void onBlocksRegistry(RegistryEvent.Register<Item> itemRegistryEvent) {
		for (ResourceLocation location : GatesItems.ITEMS.keySet()) {
			Item item = GatesItems.ITEMS.get(location);
			item.setRegistryName(location);
			itemRegistryEvent.getRegistry().register(item);
		}
	}



	private static Item addItem(String name, Item item) {
		GatesItems.ITEMS.put(new ResourceLocation(Gates.MODID, name), item);
		return item;
	}

}
