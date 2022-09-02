package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.menu.AdvancedRedstoneClockMenu;
import com.github.se7_kn8.gates.menu.FrequencyMenu;
import com.github.se7_kn8.gates.menu.PortableRedstoneTransmitterMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Gates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatesMenus {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Gates.MODID);
	public static final RegistryObject<MenuType<FrequencyMenu>> FREQUENCY_MENU_TYPE = addContainerType("transmitter",FrequencyMenu::new);

	public static final RegistryObject<MenuType<PortableRedstoneTransmitterMenu>> PORTABLE_TRANSMITTER_MENU_TYPE = addContainerType("portable_transmitter", PortableRedstoneTransmitterMenu::new);

	public static final RegistryObject<MenuType<AdvancedRedstoneClockMenu>> ADVANCED_REDSTONE_CLOCK_MENU_TYPE = addContainerType("advanced_redstone_clock", AdvancedRedstoneClockMenu::new);

	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> addContainerType(String name, MenuType.MenuSupplier<T> supplier) {
		return GatesMenus.MENU_TYPES.register(name, () -> new MenuType<T>(supplier));

	}

}
