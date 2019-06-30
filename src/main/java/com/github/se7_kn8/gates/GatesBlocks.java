package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.block.CustomRepeater;
import com.github.se7_kn8.gates.block.TwoInputLogicGate;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Gates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatesBlocks {

	public static final Map<ResourceLocation, Block> BLOCKS = new HashMap<>();

	public static final Block AND_GATE = addBlock("and_gate", new TwoInputLogicGate((x1, x2) -> x1 && x2), Gates.GATES_ITEM_GROUP);
	public static final Block OR_GATE = addBlock("or_gate", new TwoInputLogicGate((x1, x2) -> x1 || x2), Gates.GATES_ITEM_GROUP);
	public static final Block NAND_GATE = addBlock("nand_gate", new TwoInputLogicGate((x1, x2) -> !(x1 && x2)), Gates.GATES_ITEM_GROUP);
	public static final Block NOR_GATE = addBlock("nor_gate", new TwoInputLogicGate((x1, x2) -> !(x1 || x2)), Gates.GATES_ITEM_GROUP);
	public static final Block XOR_GATE = addBlock("xor_gate", new TwoInputLogicGate((x1, x2) -> x1 ^ x2), Gates.GATES_ITEM_GROUP);
	public static final Block XNOR_GATE = addBlock("xnor_gate", new TwoInputLogicGate((x1, x2) -> !(x1 ^ x2)), Gates.GATES_ITEM_GROUP);

	public static final Block FAST_REPEATER = addBlock("fast_repeater", new CustomRepeater(1), Gates.GATES_ITEM_GROUP);
	public static final Block SLOW_REPEATER = addBlock("slow_repeater", new CustomRepeater(4), Gates.GATES_ITEM_GROUP);

	// TODO public static final Block INSTANT_REPEATER = addBlock("instant_repeater", new InstantRepeater(), Gates.GATES_ITEM_GROUP);

	@SubscribeEvent
	public static void onBlocksRegistry(RegistryEvent.Register<Block> blockRegistryEvent) {
		for (ResourceLocation location : GatesBlocks.BLOCKS.keySet()) {
			Block block = GatesBlocks.BLOCKS.get(location);
			block.setRegistryName(location);
			blockRegistryEvent.getRegistry().register(block);
		}
	}

	private static Block addBlock(String name, Block block, ItemGroup tab) {
		GatesBlocks.BLOCKS.put(new ResourceLocation(Gates.MODID, name), block);
		BlockItem itemBlock = new BlockItem(block, new Item.Properties().group(tab));
		itemBlock.addToBlockToItemMap(Item.BLOCK_TO_ITEM, itemBlock);
		GatesItems.ITEMS.put(new ResourceLocation(Gates.MODID, name), itemBlock);
		return block;
	}

}
