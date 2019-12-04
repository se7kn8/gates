package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.container.FrequencyContainer;
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

	public static final ContainerType<FrequencyContainer> TRANSMITTER_CONTAINER_TYPE = addContainerType("transmitter", IForgeContainerType.create((windowId, inv, data) -> new FrequencyContainer(windowId, inv.player.world, data.readBlockPos(), inv, inv.player)));

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
