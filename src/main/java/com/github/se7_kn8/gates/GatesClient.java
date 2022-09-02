package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.client.screen.AdvancedRedstoneClockScreen;
import com.github.se7_kn8.gates.client.screen.FrequencyScreen;
import com.github.se7_kn8.gates.client.screen.PortableTransmitterScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Gates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatesClient {
	@SubscribeEvent
	public static void registerColors(final RegisterColorHandlersEvent.Block event) {
		event.register((state, reader, pos, tint) -> RedStoneWireBlock.getColorForPower(state.getValue(RedStoneWireBlock.POWER)), GatesBlocks.WAXED_REDSTONE_WIRE.get());
	}

	@SubscribeEvent
	public static void doClientStuff(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(GatesMenus.FREQUENCY_MENU_TYPE.get(), FrequencyScreen::new);
			MenuScreens.register(GatesMenus.PORTABLE_TRANSMITTER_MENU_TYPE.get(), PortableTransmitterScreen::new);
			MenuScreens.register(GatesMenus.ADVANCED_REDSTONE_CLOCK_MENU_TYPE.get(), AdvancedRedstoneClockScreen::new);
		});
	}

}
