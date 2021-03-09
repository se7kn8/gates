package com.github.se7_kn8.gates;

import com.github.se7_kn8.gates.block.*;
import com.github.se7_kn8.gates.block.redstone_clock.AdvancedRedstoneClock;
import com.github.se7_kn8.gates.block.redstone_clock.RedstoneClock;
import com.github.se7_kn8.gates.block.wireless_redstone.ReceiverBlock;
import com.github.se7_kn8.gates.block.wireless_redstone.TransmitterBlock;
import com.github.se7_kn8.gates.block.wireless_redstone.WirelessRedstoneLamp;
import com.github.se7_kn8.gates.tile.CustomDetectorTile;
import com.github.se7_kn8.gates.tile.ReceiverTileEntity;
import com.github.se7_kn8.gates.tile.RedstoneClockTileEntity;
import com.github.se7_kn8.gates.tile.TransmitterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class GatesBlocks {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Gates.MODID);
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Gates.MODID);

	public static final RegistryObject<Block> REDSTONE_BLOCK_OFF = addBlock("redstone_block_off", () -> new Block(Block.Properties.from(Blocks.REDSTONE_BLOCK)), Gates.GATES_ITEM_GROUP);

	public static final RegistryObject<Block> AND_GATE = addBlock("and_gate", () -> new TwoInputLogicGate((x1, x2) -> x1 && x2), Gates.GATES_ITEM_GROUP);
	public static final RegistryObject<Block> OR_GATE = addBlock("or_gate", () -> new TwoInputLogicGate((x1, x2) -> x1 || x2), Gates.GATES_ITEM_GROUP);
	public static final RegistryObject<Block> NAND_GATE = addBlock("nand_gate", () -> new TwoInputLogicGate((x1, x2) -> !(x1 && x2)), Gates.GATES_ITEM_GROUP);
	public static final RegistryObject<Block> NOR_GATE = addBlock("nor_gate", () -> new TwoInputLogicGate((x1, x2) -> !(x1 || x2)), Gates.GATES_ITEM_GROUP);
	public static final RegistryObject<Block> XOR_GATE = addBlock("xor_gate", () -> new TwoInputLogicGate((x1, x2) -> x1 ^ x2), Gates.GATES_ITEM_GROUP);
	public static final RegistryObject<Block> XNOR_GATE = addBlock("xnor_gate", () -> new TwoInputLogicGate((x1, x2) -> !(x1 ^ x2)), Gates.GATES_ITEM_GROUP);
	public static final RegistryObject<Block> NOT_GATE = addBlock("not_gate", () -> new OneInputLogicGate(x1 -> !x1), Gates.GATES_ITEM_GROUP);

	public static final RegistryObject<Block> FAST_REPEATER = addBlock("fast_repeater", () -> new CustomRepeater(1), Gates.GATES_ITEM_GROUP);
	public static final RegistryObject<Block> SLOW_REPEATER = addBlock("slow_repeater", () -> new CustomRepeater(4), Gates.GATES_ITEM_GROUP);

	public static final RegistryObject<Block> WIRELESS_REDSTONE_RECEIVER = addBlock("receiver", ReceiverBlock::new, Gates.GATES_ITEM_GROUP);
	public static final RegistryObject<Block> WIRELESS_REDSTONE_TRANSMITTER = addBlock("transmitter", TransmitterBlock::new, Gates.GATES_ITEM_GROUP);

	public static final RegistryObject<Block> WIRELESS_REDSTONE_LAMP = addBlock("wireless_redstone_lamp", WirelessRedstoneLamp::new, Gates.GATES_ITEM_GROUP);

	public static final RegistryObject<Block> RAIN_DETECTOR = addBlock("rain_detector", () -> new CustomDetector((blockState, world, blockPos) -> world.isRainingAt(blockPos.up(2)) ? 15 : 0), Gates.GATES_ITEM_GROUP);
	public static final RegistryObject<Block> THUNDER_DETECTOR = addBlock("thunder_detector", () -> new CustomDetector((blockState, world, blockPos) -> world.isThundering() ? 15 : 0), Gates.GATES_ITEM_GROUP);

	public static final RegistryObject<Block> REDSTONE_CLOCK = addBlock("redstone_clock", RedstoneClock::new, Gates.GATES_ITEM_GROUP);
	public static final RegistryObject<Block> ADVANCED_REDSTONE_CLOCK = addBlock("advanced_redstone_clock", AdvancedRedstoneClock::new, Gates.GATES_ITEM_GROUP);

	public static final RegistryObject<Block> ROTARY_SWITCH = addBlock("rotary_switch", RotarySwitch::new, Gates.GATES_ITEM_GROUP);

	public static final RegistryObject<Block> RS_FLIP_FLOP = addBlock("rs_flip_flop", RSFlipFlop::new, Gates.GATES_ITEM_GROUP);

	// TODO not finished yet
	public static final RegistryObject<Block> WAXED_REDSTONE_WIRE = addBlock("waxed_redstone_wire", WaxedRedstone::new, Gates.GATES_ITEM_GROUP);

	// TODO contains a log of bugs
	//public static final Block D_FLIP_FLOP = addBlock("t_flip_flop", new TFlipFlop(), Gates.GATES_ITEM_GROUP);

	public static final RegistryObject<TileEntityType<CustomDetectorTile>> RAIN_DETECTOR_TILE_ENTITY = addTileEntity("rain_detector", CustomDetectorTile::new, RAIN_DETECTOR, THUNDER_DETECTOR);

	public static final RegistryObject<TileEntityType<ReceiverTileEntity>> RECEIVER_TILE_ENTITY_TYPE = addTileEntity("receiver", ReceiverTileEntity::new, WIRELESS_REDSTONE_RECEIVER);
	public static final RegistryObject<TileEntityType<TransmitterTileEntity>> TRANSMITTER_TILE_ENTITY_TYPE = addTileEntity("transmitter", TransmitterTileEntity::new, WIRELESS_REDSTONE_TRANSMITTER, WIRELESS_REDSTONE_LAMP);
	public static final RegistryObject<TileEntityType<RedstoneClockTileEntity>> REDSTONE_CLOCK_TILE_ENTITY_TYPE = addTileEntity("redstone_clock", RedstoneClockTileEntity::new, REDSTONE_CLOCK, ADVANCED_REDSTONE_CLOCK);


	private static <T extends Block> RegistryObject<T> addBlock(String name, Supplier<T> block, ItemGroup tab) {
		RegistryObject<T> object = GatesBlocks.BLOCKS.register(name, block);
		GatesItems.ITEMS.register(name, () -> {
			BlockItem itemBlock = new BlockItem(object.get(), new Item.Properties().group(tab));
			itemBlock.addToBlockToItemMap(Item.BLOCK_TO_ITEM, itemBlock);
			return itemBlock;
		});
		return object;
	}

	@SafeVarargs
	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> addTileEntity(String name, Supplier<T> tileEntitySupplier, Supplier<? extends Block>... validBlocksSuppliers) {
		return GatesBlocks.TILE_ENTITIES.register(name, () -> {

			Block[] blocks = new Block[validBlocksSuppliers.length];
			for (int i = 0; i < validBlocksSuppliers.length; i++) {
				blocks[i] = validBlocksSuppliers[i].get();
			}
			return TileEntityType.Builder.create(tileEntitySupplier, blocks).build(null);
		});
	}

}
