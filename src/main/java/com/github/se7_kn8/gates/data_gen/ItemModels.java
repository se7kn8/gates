package com.github.se7_kn8.gates.data_gen;


import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.GatesItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;


public class ItemModels extends ItemModelProvider {

	public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Gates.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		addSimpleItemModel(GatesBlocks.ADVANCED_REDSTONE_CLOCK);
		addSimpleItemModel(GatesBlocks.AND_GATE);
		addSimpleItemModel(GatesBlocks.FAST_REPEATER);
		addSimpleItemModel(GatesItems.FREQUENCY_CHANGER);
		addSimpleItemModel(GatesBlocks.NAND_GATE);
		addSimpleItemModel(GatesBlocks.NOR_GATE);
		addSimpleItemModel(GatesBlocks.NOT_GATE);
		addSimpleItemModel(GatesBlocks.OR_GATE);
		addSimpleItemModel(GatesItems.PORTABLE_REDSTONE_TRANSMITTER);
		addSimpleItemModel(GatesBlocks.WIRELESS_REDSTONE_RECEIVER);
		addSimpleItemModel(GatesBlocks.REDSTONE_CLOCK);
		addSimpleItemModel(GatesItems.REDSTONE_TORCH_PEARL);
		addSimpleItemModel(GatesBlocks.ROTARY_SWITCH);
		addSimpleItemModel(GatesBlocks.RS_FLIP_FLOP);
		addSimpleItemModel(GatesBlocks.SLOW_REPEATER);
		addSimpleItemModel(GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER);
		addSimpleItemModel(GatesBlocks.XNOR_GATE);
		addSimpleItemModel(GatesBlocks.XOR_GATE);

		addSimpleBlockItemModel(GatesBlocks.RAIN_DETECTOR, "sensor/rain_detector");
		addSimpleBlockItemModel(GatesBlocks.THUNDER_DETECTOR, "sensor/thunder_detector");
		addSimpleBlockItemModel(GatesBlocks.REDSTONE_BLOCK_OFF, "redstone_block/redstone_block_off");
		addSimpleBlockItemModel(GatesBlocks.WIRELESS_REDSTONE_LAMP, "wireless/lamp_off");

	}

	private void addSimpleItemModel(IItemProvider item) {
		String name = item.asItem().getRegistryName().getPath();
		withExistingParent(name, mcLoc("item/generated")).texture("layer0", modLoc("item/" + name));
	}

	private void addSimpleBlockItemModel(Block block, String parent) {
		withExistingParent(block.getRegistryName().getPath(), modLoc("block/" + parent));
	}

}
