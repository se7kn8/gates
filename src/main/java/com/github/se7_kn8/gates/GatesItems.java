package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.item.FrequencyChangerItem;
import com.github.se7_kn8.gates.item.PortableRedstoneTransmitter;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class GatesItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Gates.MODID);

	public static final RegistryObject<Item> REDSTONE_TORCH_PEARL = addItem("redstone_torch_pearl", () -> new Item(new Item.Properties().group(Gates.GATES_ITEM_GROUP)));

	public static final RegistryObject<Item> FREQUENCY_CHANGER = addItem("frequency_changer", FrequencyChangerItem::new);

	public static final RegistryObject<Item> PORTABLE_REDSTONE_TRANSMITTER = addItem("portable_redstone_transmitter", PortableRedstoneTransmitter::new);


	private static <T extends Item> RegistryObject<T> addItem(String name, Supplier<T> item) {
		return GatesItems.ITEMS.register(name, item);
	}

}
