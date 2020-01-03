package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.client.screen.FrequencyScreen;
import com.github.se7_kn8.gates.client.screen.PortableTransmitterScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Gates.MODID)
public class Gates {
	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "gates";

	public static ItemGroup GATES_ITEM_GROUP = new ItemGroup("gates") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(GatesBlocks.XNOR_GATE);
		}
	};

	public Gates() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		CapabilityWirelessNode.register();
		PacketHandler.init();
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		ScreenManager.registerFactory(GatesContainers.TRANSMITTER_CONTAINER_TYPE, FrequencyScreen::new);
		ScreenManager.registerFactory(GatesContainers.PORTABLE_TRANSMITTER_CONTAINER_TYPE, PortableTransmitterScreen::new);
	}

}
