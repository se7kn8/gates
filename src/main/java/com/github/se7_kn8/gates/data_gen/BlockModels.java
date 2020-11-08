package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.Gates;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModels extends BlockModelProvider {

	public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Gates.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		addSimpleCubeModel("wireless/lamp_on", "block/wireless_redstone_lamp_on");
		addSimpleCubeModel("wireless/lamp_off", "block/wireless_redstone_lamp_off");

		addSimpleCubeModel("redstone_block/redstone_block_off", "block/redstone_block_off");
		addSimpleModel("redstone_block/small_redstone_block_off", modLoc("block/redstone_block/small_redstone_block"), "block", modLoc("block/redstone_block_off"));
		addSimpleModel("redstone_block/small_redstone_block_on", modLoc("block/redstone_block/small_redstone_block"), "block", mcLoc("block/redstone_block"));

		addSimpleModel("redstone_clock/redstone_clock", modLoc("block/gate/basic_gate"), "top", modLoc("block/redstone_clock"));
		withExistingParent("block/redstone_clock/advanced_redstone_clock_off", modLoc("block/gate/basic_gate")).texture("top", modLoc("block/advanced_redstone_clock_off")).texture("slap", mcLoc("block/gold_block"));
		withExistingParent("block/redstone_clock/advanced_redstone_clock_on", modLoc("block/gate/basic_gate")).texture("top", modLoc("block/advanced_redstone_clock_on")).texture("slap", mcLoc("block/gold_block"));

		addSimpleModel("sensor/rain_detector", modLoc("block/sensor/basic_detector"), "top", modLoc("block/rain_detector_top"));
		addSimpleModel("sensor/rain_detector_inverted", modLoc("block/sensor/basic_detector"), "top", modLoc("block/rain_detector_top_inverted"));
		addSimpleModel("sensor/thunder_detector", modLoc("block/sensor/basic_detector"), "top", modLoc("block/thunder_detector_top"));
		addSimpleModel("sensor/thunder_detector_inverted", modLoc("block/sensor/basic_detector"), "top", modLoc("block/thunder_detector_top_inverted"));

		addGateModel("wireless/receiver", "block/receiver");
		addGateModel("wireless/transmitter", "block/transmitter");
		addGateModel("gate/and_gate", "block/and_gate");
		addGateModel("gate/nand_gate", "block/nand_gate");
		addGateModel("gate/nor_gate", "block/nor_gate");
		addGateModel("gate/not_gate", "block/not_gate");
		addGateModel("gate/or_gate", "block/or_gate");
		addGateModel("gate/xnor_gate", "block/xnor_gate");
		addGateModel("gate/xor_gate", "block/xor_gate");

		addOnOffTorch("input_torch");
		addOnOffTorch("left_torch");
		addOnOffTorch("right_torch");
		addOnOffTorch("output_torch");
		addOnOffTorch("main_torch");
		addOnOffTorch("main_torch_1");
		addOnOffTorch("main_torch_2");
		addOnOffTorch("main_torch_3");
		addOnOffTorch("main_torch_4");
		addOnOffTorch("main_torch_5");
	}


	public void addGateModel(String path, String texture) {
		withExistingParent("block/" + path, modLoc("block/gate/basic_gate")).texture("top", modLoc(texture));
	}

	public void addSimpleCubeModel(String path, String texture) {
		withExistingParent("block/" + path, mcLoc("block/cube_all")).texture("all", modLoc(texture));
	}

	public void addSimpleModel(String path, ResourceLocation parent, String textureKey, ResourceLocation texture) {
		withExistingParent("block/" + path, parent).texture(textureKey, texture);
	}

	public void addOnOffTorch(String path) {
		withExistingParent("block/torch/" + path + "_on", modLoc("block/torch/" + path)).texture("texture", mcLoc("block/redstone_torch"));
		withExistingParent("block/torch/" + path + "_off", modLoc("block/torch/" + path)).texture("texture", mcLoc("block/redstone_torch_off"));
	}

}
