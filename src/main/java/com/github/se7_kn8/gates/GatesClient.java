package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.client.screen.AdvancedRedstoneClockScreen;
import com.github.se7_kn8.gates.client.screen.FrequencyScreen;
import com.github.se7_kn8.gates.client.screen.PortableTransmitterScreen;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
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
		colors.register((state, reader, pos, tint) -> RedstoneWireBlock.getColorForPower(state.getValue(RedstoneWireBlock.POWER)), GatesBlocks.WAXED_REDSTONE_WIRE.get());
	}

	@SubscribeEvent
	public static void doClientStuff(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ScreenManager.register(GatesContainers.FREQUENCY_CONTAINER_TYPE, FrequencyScreen::new);
			ScreenManager.register(GatesContainers.PORTABLE_TRANSMITTER_CONTAINER_TYPE, PortableTransmitterScreen::new);
			ScreenManager.register(GatesContainers.ADVANCED_REDSTONE_CLOCK_CONTAINER_TYPE, AdvancedRedstoneClockScreen::new);
			RenderTypeLookup.setRenderLayer(GatesBlocks.AND_GATE.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.OR_GATE.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.XOR_GATE.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.NAND_GATE.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.NOR_GATE.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.XNOR_GATE.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.NOT_GATE.get(), RenderType.cutout());

			RenderTypeLookup.setRenderLayer(GatesBlocks.FAST_REPEATER.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.SLOW_REPEATER.get(), RenderType.cutout());

			RenderTypeLookup.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_RECEIVER.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.WIRELESS_REDSTONE_LAMP.get(), RenderType.cutout());


			RenderTypeLookup.setRenderLayer(GatesBlocks.REDSTONE_CLOCK.get(), RenderType.cutout());
			RenderTypeLookup.setRenderLayer(GatesBlocks.ADVANCED_REDSTONE_CLOCK.get(), RenderType.cutout());

			RenderTypeLookup.setRenderLayer(GatesBlocks.WAXED_REDSTONE_WIRE.get(), RenderType.translucent());
		});
	}

}
