package com.github.se7_kn8.gates;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
	}

	private void setup(final FMLCommonSetupEvent event) {
		// TODO
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// TODO
	}
}
