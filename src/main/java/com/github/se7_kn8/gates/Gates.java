package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.client.screen.AdvancedRedstoneClockScreen;
import com.github.se7_kn8.gates.client.screen.FrequencyScreen;
import com.github.se7_kn8.gates.client.screen.PortableTransmitterScreen;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Gates.MODID)
public class Gates {
	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "gates";

	public static final GatesConfig config = new GatesConfig();

	public static ItemGroup GATES_ITEM_GROUP = new ItemGroup("gates") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(GatesBlocks.XNOR_GATE.get());
		}

	};

	public Gates() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerColors);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Gates.config.defaultConfig);
		GatesBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		GatesBlocks.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		GatesItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(GatesBlocks.class);
	}

	private void setup(final FMLCommonSetupEvent event) {
		CapabilityWirelessNode.register();
		event.enqueueWork(PacketHandler::init);
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ScreenManager.registerFactory(GatesContainers.FREQUENCY_CONTAINER_TYPE, FrequencyScreen::new);
			ScreenManager.registerFactory(GatesContainers.PORTABLE_TRANSMITTER_CONTAINER_TYPE, PortableTransmitterScreen::new);
			ScreenManager.registerFactory(GatesContainers.ADVANCED_REDSTONE_CLOCK_CONTAINER_TYPE, AdvancedRedstoneClockScreen::new);
			RenderTypeLookup.setRenderLayer(GatesBlocks.AND_GATE.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.OR_GATE.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.XOR_GATE.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.NAND_GATE.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.NOR_GATE.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.XNOR_GATE.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.NOT_GATE.get(), RenderType.getCutout());

			RenderTypeLookup.setRenderLayer(GatesBlocks.FAST_REPEATER.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.SLOW_REPEATER.get(), RenderType.getCutout());

			RenderTypeLookup.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_RECEIVER.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_LAMP.get(), RenderType.getCutout());


			RenderTypeLookup.setRenderLayer(GatesBlocks.REDSTONE_CLOCK.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.ADVANCED_REDSTONE_CLOCK.get(), RenderType.getCutout());

			RenderTypeLookup.setRenderLayer(GatesBlocks.WAXED_REDSTONE_WIRE.get(), RenderType.getTranslucent());
		});
	}

	private void registerColors(ColorHandlerEvent.Block event) {
		BlockColors colors = event.getBlockColors();

		colors.register((state, reader, pos, tint) -> RedstoneWireBlock.getRGBByPower(state.get(RedstoneWireBlock.POWER)), GatesBlocks.WAXED_REDSTONE_WIRE.get());
	}

}
