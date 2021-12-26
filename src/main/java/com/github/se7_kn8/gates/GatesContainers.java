package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.container.AdvancedRedstoneClockMenu;
import com.github.se7_kn8.gates.container.FrequencyMenu;
import com.github.se7_kn8.gates.container.PortableRedstoneTransmitterContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Gates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatesContainers {

	public static final List<MenuType<? extends AbstractContainerMenu>> CONTAINERS = new ArrayList<>();

	public static final MenuType<FrequencyMenu> FREQUENCY_MENU_TYPE = addContainerType("transmitter", FrequencyMenu::new);

	public static final MenuType<PortableRedstoneTransmitterContainer> PORTABLE_TRANSMITTER_MENU_TYPE = addContainerType("portable_transmitter", PortableRedstoneTransmitterContainer::new);

	public static final MenuType<AdvancedRedstoneClockMenu> ADVANCED_REDSTONE_CLOCK_MENU_TYPE = addContainerType("advanced_redstone_clock", AdvancedRedstoneClockMenu::new);

	@SubscribeEvent
	public static void onContainerRegistry(RegistryEvent.Register<MenuType<?>> containerTypeRegistryEvent) {
		for (MenuType<?> type : GatesContainers.CONTAINERS) {
			containerTypeRegistryEvent.getRegistry().register(type);
		}
	}

	private static <T extends AbstractContainerMenu> MenuType<T> addContainerType(String name, MenuType.MenuSupplier<T> supplier) {
		MenuType<T> menuType = new MenuType<>(supplier);
		menuType.setRegistryName(name);
		GatesContainers.CONTAINERS.add(menuType);
		return menuType;
	}

}
