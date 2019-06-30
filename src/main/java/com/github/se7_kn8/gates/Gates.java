package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Gates.MODID)
public class Gates {
	private static final Logger LOGGER = LogManager.getLogger();

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
		// TODO
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// TODO
	}


	@SubscribeEvent
	public static void onPlayer(ServerChatEvent event) {
		System.out.println("Received message: " + event.getMessage());
		if (event.getMessage().startsWith("freq:")) {
			RedstoneReceiverWorldSavedData.get(event.getPlayer().getServerWorld()).setFrequency(event.getPlayer().getServerWorld(), 1234, 15);
		}
	}

}
