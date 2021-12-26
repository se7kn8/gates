package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.client.screen.AdvancedRedstoneClockScreen;
import com.github.se7_kn8.gates.client.screen.FrequencyScreen;
import com.github.se7_kn8.gates.client.screen.PortableTransmitterScreen;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Gates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatesClient {
	@SubscribeEvent
	public static void registerColors(final ColorHandlerEvent.Block event) {
		BlockColors colors = event.getBlockColors();
		colors.register((state, reader, pos, tint) -> RedStoneWireBlock.getColorForPower(state.getValue(RedStoneWireBlock.POWER)), GatesBlocks.WAXED_REDSTONE_WIRE.get());
	}

	@SubscribeEvent
	public static void doClientStuff(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(GatesContainers.FREQUENCY_MENU_TYPE, FrequencyScreen::new);
			MenuScreens.register(GatesContainers.PORTABLE_TRANSMITTER_MENU_TYPE, PortableTransmitterScreen::new);
			MenuScreens.register(GatesContainers.ADVANCED_REDSTONE_CLOCK_MENU_TYPE, AdvancedRedstoneClockScreen::new);
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.AND_GATE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.OR_GATE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.XOR_GATE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.NAND_GATE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.NOR_GATE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.XNOR_GATE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.NOT_GATE.get(), RenderType.cutout());

			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.FAST_REPEATER.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.SLOW_REPEATER.get(), RenderType.cutout());

			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_RECEIVER.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_LAMP.get(), RenderType.cutout());


			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.REDSTONE_CLOCK.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.ADVANCED_REDSTONE_CLOCK.get(), RenderType.cutout());

			ItemBlockRenderTypes.setRenderLayer(GatesBlocks.WAXED_REDSTONE_WIRE.get(), RenderType.translucent());
		});
	}

}
