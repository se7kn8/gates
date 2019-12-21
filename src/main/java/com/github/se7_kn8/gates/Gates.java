package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.client.screen.FrequencyScreen;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
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
		RenderTypeLookup.setRenderLayer(GatesBlocks.AND_GATE, RenderType.func_228643_e_());
		RenderTypeLookup.setRenderLayer(GatesBlocks.OR_GATE, RenderType.func_228643_e_());
		RenderTypeLookup.setRenderLayer(GatesBlocks.XOR_GATE, RenderType.func_228643_e_());
		RenderTypeLookup.setRenderLayer(GatesBlocks.NAND_GATE, RenderType.func_228643_e_());
		RenderTypeLookup.setRenderLayer(GatesBlocks.NOR_GATE, RenderType.func_228643_e_());
		RenderTypeLookup.setRenderLayer(GatesBlocks.XNOR_GATE, RenderType.func_228643_e_());
		RenderTypeLookup.setRenderLayer(GatesBlocks.NOT_GATE, RenderType.func_228643_e_());

		RenderTypeLookup.setRenderLayer(GatesBlocks.FAST_REPEATER, RenderType.func_228643_e_());
		RenderTypeLookup.setRenderLayer(GatesBlocks.SLOW_REPEATER, RenderType.func_228643_e_());

		RenderTypeLookup.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_RECEIVER, RenderType.func_228643_e_());
		RenderTypeLookup.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER, RenderType.func_228643_e_());

	}

}
