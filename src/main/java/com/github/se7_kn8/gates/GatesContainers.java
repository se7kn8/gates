package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.container.AdvancedRedstoneClockContainer;
import com.github.se7_kn8.gates.container.FrequencyContainer;
import com.github.se7_kn8.gates.container.PortableRedstoneTransmitterContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Gates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatesContainers {

	public static final List<ContainerType<? extends Container>> CONTAINERS = new ArrayList<>();

	public static final ContainerType<FrequencyContainer> FREQUENCY_CONTAINER_TYPE = addContainerType("transmitter", IForgeContainerType.create((windowId, inv, data) -> new FrequencyContainer(windowId, inv.player.world, data.readBlockPos(), inv, inv.player)));

	public static final ContainerType<PortableRedstoneTransmitterContainer> PORTABLE_TRANSMITTER_CONTAINER_TYPE = addContainerType("portable_transmitter", new ContainerType<>((id, inv) -> new PortableRedstoneTransmitterContainer(id)));

	public static final ContainerType<AdvancedRedstoneClockContainer> ADVANCED_REDSTONE_CLOCK_CONTAINER_TYPE = addContainerType("advanced_redstone_clock", IForgeContainerType.create((windowId, inv, data) -> new AdvancedRedstoneClockContainer(windowId, inv.player.world, data.readBlockPos(), inv, inv.player)));

	@SubscribeEvent
	public static void onContainerRegistry(RegistryEvent.Register<ContainerType<?>> containerTypeRegistryEvent) {
		for (ContainerType<?> type : GatesContainers.CONTAINERS) {
			containerTypeRegistryEvent.getRegistry().register(type);
		}
	}

	private static <T extends Container> ContainerType<T> addContainerType(String name, ContainerType<T> type) {
		type.setRegistryName(name);
		GatesContainers.CONTAINERS.add(type);
		return type;
	}

}
