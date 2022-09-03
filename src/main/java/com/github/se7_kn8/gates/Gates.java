package com.github.se7_kn8.gates;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Gates.MODID)
public class Gates {
	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "gates";

	public static final GatesConfig config = new GatesConfig();

	public static CreativeModeTab GATES_ITEM_GROUP = new CreativeModeTab("gates") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(GatesBlocks.XNOR_GATE.get());
		}

	};

	public Gates() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Gates.config.defaultConfig);
		GatesBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		GatesBlocks.BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
		GatesItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		GatesMenus.MENU_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(PacketHandler::init);
	}
}
