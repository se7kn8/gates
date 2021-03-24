package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.block.*;
import com.github.se7_kn8.gates.block.redstone_clock.AdvancedRedstoneClock;
import com.github.se7_kn8.gates.block.redstone_clock.RedstoneClock;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {
	public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Gates.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		cubeAll(GatesBlocks.REDSTONE_BLOCK_OFF.get(), modLoc("block/redstone_block_off"));

		gateModel(GatesBlocks.AND_GATE.get());
		gateModel(GatesBlocks.OR_GATE.get());
		gateModel(GatesBlocks.XOR_GATE.get());
		gateModel(GatesBlocks.NAND_GATE.get());
		gateModel(GatesBlocks.NOR_GATE.get());
		gateModel(GatesBlocks.XNOR_GATE.get());
		notGate(GatesBlocks.NOT_GATE.get());

		detectorModel(GatesBlocks.RAIN_DETECTOR.get());
		detectorModel(GatesBlocks.THUNDER_DETECTOR.get());

		wirelessRedstoneModel(GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER.get());
		wirelessRedstoneModel(GatesBlocks.WIRELESS_REDSTONE_RECEIVER.get());
		lamp(GatesBlocks.WIRELESS_REDSTONE_LAMP.get());

		redstoneClock(GatesBlocks.REDSTONE_CLOCK.get());
		advancedRedstoneClock(GatesBlocks.ADVANCED_REDSTONE_CLOCK.get());

		rsFlipFlop(GatesBlocks.RS_FLIP_FLOP.get());

		rotarySwitch(GatesBlocks.ROTARY_SWITCH.get());

		repeater(GatesBlocks.FAST_REPEATER.get());
		repeater(GatesBlocks.SLOW_REPEATER.get());

		waxedRedstone(GatesBlocks.WAXED_REDSTONE_WIRE.get());
	}

	private void cubeAll(Block b, ResourceLocation texture) {
		simpleBlock(b);
		simpleBlockItem(b, models().cubeAll(b.getRegistryName().getPath(), texture));
	}

	private void simpleItem(String name) {
		itemModels().singleTexture(name, mcLoc("item/generated"), "layer0", modLoc("item/" + name));
	}

	private ModelFile getTorchModel(String name, boolean on) {
		if (on) {
			return models().withExistingParent("block/torch/" + name + "_on", modLoc("block/torch/" + name)).texture("texture", mcLoc("block/redstone_torch"));
		} else {
			return models().withExistingParent("block/torch/" + name + "_off", modLoc("block/torch/" + name)).texture("texture", mcLoc("block/redstone_torch_off"));
		}
	}

	private void detectorModel(Block b) {
		String name = b.getRegistryName().getPath();
		VariantBlockStateBuilder builder = getVariantBuilder(b);
		ModelFile detector = models().withExistingParent("block/sensor/" + name, modLoc("block/sensor/basic_detector")).texture("top", modLoc("block/" + name + "_top"));
		ModelFile detector_inverted = models().withExistingParent("block/sensor/" + name + "_inverted", modLoc("block/sensor/basic_detector")).texture("top", modLoc("block/" + name + "_top_inverted"));
		builder.forAllStatesExcept(blockState -> ConfiguredModel.builder().modelFile(blockState.getValue(CustomDetector.INVERTED) ? detector_inverted : detector).build(), CustomDetector.POWER);
		simpleBlockItem(b, detector);
	}

	private void gateModel(Block b) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(b);

		String name = b.getRegistryName().getPath();
		ModelFile baseModel = models().withExistingParent("block/gate/" + name, modLoc("block/gate/basic_gate")).texture("top", modLoc("block/" + name));
		ModelFile leftTorchOn = getTorchModel("left_torch", true);
		ModelFile leftTorchOff = getTorchModel("left_torch", false);
		ModelFile rightTorchOn = getTorchModel("right_torch", true);
		ModelFile rightTorchOff = getTorchModel("right_torch", false);
		ModelFile outputTorchOn = getTorchModel("output_torch", true);
		ModelFile outputTorchOff = getTorchModel("output_torch", false);

		for (Direction dir : TwoInputLogicGate.FACING.getPossibleValues()) {
			int yRot = (int) dir.toYRot();
			builder.part().modelFile(baseModel).rotationY(yRot).addModel().condition(TwoInputLogicGate.FACING, dir);
			builder.part().modelFile(leftTorchOn).rotationY(yRot).addModel().condition(TwoInputLogicGate.FACING, dir).condition(TwoInputLogicGate.LEFT_INPUT, true);
			builder.part().modelFile(leftTorchOff).rotationY(yRot).addModel().condition(TwoInputLogicGate.FACING, dir).condition(TwoInputLogicGate.LEFT_INPUT, false);
			builder.part().modelFile(rightTorchOn).rotationY(yRot).addModel().condition(TwoInputLogicGate.FACING, dir).condition(TwoInputLogicGate.RIGHT_INPUT, true);
			builder.part().modelFile(rightTorchOff).rotationY(yRot).addModel().condition(TwoInputLogicGate.FACING, dir).condition(TwoInputLogicGate.RIGHT_INPUT, false);
			builder.part().modelFile(outputTorchOn).rotationY(yRot).addModel().condition(TwoInputLogicGate.FACING, dir).condition(TwoInputLogicGate.POWERED, true);
			builder.part().modelFile(outputTorchOff).rotationY(yRot).addModel().condition(TwoInputLogicGate.FACING, dir).condition(TwoInputLogicGate.POWERED, false);
		}

		simpleItem(name);
	}

	private void notGate(Block b) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(b);

		String name = b.getRegistryName().getPath();
		ModelFile baseModel = models().withExistingParent("block/gate/" + name, modLoc("block/gate/basic_gate")).texture("top", modLoc("block/" + name));
		ModelFile inputTorchOn = getTorchModel("input_torch", true);
		ModelFile inputTorchOff = getTorchModel("input_torch", false);
		ModelFile outputTorchOn = getTorchModel("output_torch", true);
		ModelFile outputTorchOff = getTorchModel("output_torch", false);


		for (Direction dir : OneInputLogicGate.FACING.getPossibleValues()) {
			int yRot = (int) dir.toYRot();
			builder.part().modelFile(baseModel).rotationY(yRot).addModel().condition(OneInputLogicGate.FACING, dir);
			builder.part().modelFile(inputTorchOn).rotationY(yRot).addModel().condition(OneInputLogicGate.FACING, dir).condition(OneInputLogicGate.INPUT, true);
			builder.part().modelFile(inputTorchOff).rotationY(yRot).addModel().condition(OneInputLogicGate.FACING, dir).condition(OneInputLogicGate.INPUT, false);
			builder.part().modelFile(outputTorchOn).rotationY(yRot).addModel().condition(OneInputLogicGate.FACING, dir).condition(OneInputLogicGate.POWERED, true);
			builder.part().modelFile(outputTorchOff).rotationY(yRot).addModel().condition(OneInputLogicGate.FACING, dir).condition(OneInputLogicGate.POWERED, false);
		}

		simpleItem(name);
	}

	private void wirelessRedstoneModel(Block b) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(b);
		String name = b.getRegistryName().getPath();
		ModelFile baseModel = models().withExistingParent("block/wireless/" + name, modLoc("block/gate/basic_gate")).texture("top", modLoc("block/" + name));
		ModelFile mainTorchOn = getTorchModel("main_torch", true);
		ModelFile mainTorchOff = getTorchModel("main_torch", false);

		builder.part().modelFile(baseModel).addModel();
		builder.part().modelFile(mainTorchOn).addModel().condition(BlockStateProperties.POWER, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
		builder.part().modelFile(mainTorchOff).addModel().condition(BlockStateProperties.POWER, 0);
		simpleItem(name);
	}

	private void redstoneClock(Block b) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(b);

		ModelFile mainTorch1On = getTorchModel("main_torch_1", true);
		ModelFile mainTorch1Off = getTorchModel("main_torch_1", false);
		ModelFile mainTorch2On = getTorchModel("main_torch_2", true);
		ModelFile mainTorch2Off = getTorchModel("main_torch_2", false);
		ModelFile mainTorch3On = getTorchModel("main_torch_3", true);
		ModelFile mainTorch3Off = getTorchModel("main_torch_3", false);
		ModelFile mainTorch4On = getTorchModel("main_torch_4", true);
		ModelFile mainTorch4Off = getTorchModel("main_torch_4", false);
		ModelFile mainTorch5On = getTorchModel("main_torch_5", true);
		ModelFile mainTorch5Off = getTorchModel("main_torch_5", false);
		ModelFile base = models().withExistingParent("block/redstone_clock/redstone_clock", modLoc("block/gate/basic_gate")).texture("top", modLoc("block/redstone_clock"));

		builder.part().modelFile(base).addModel();
		builder.part().modelFile(mainTorch1Off).addModel().condition(RedstoneClock.CLOCK_SPEED, 1).condition(RedstoneClock.POWERED, false);
		builder.part().modelFile(mainTorch1On).addModel().condition(RedstoneClock.CLOCK_SPEED, 1).condition(RedstoneClock.POWERED, true);
		builder.part().modelFile(mainTorch2Off).addModel().condition(RedstoneClock.CLOCK_SPEED, 2).condition(RedstoneClock.POWERED, false);
		builder.part().modelFile(mainTorch2On).addModel().condition(RedstoneClock.CLOCK_SPEED, 2).condition(RedstoneClock.POWERED, true);
		builder.part().modelFile(mainTorch3Off).addModel().condition(RedstoneClock.CLOCK_SPEED, 3).condition(RedstoneClock.POWERED, false);
		builder.part().modelFile(mainTorch3On).addModel().condition(RedstoneClock.CLOCK_SPEED, 3).condition(RedstoneClock.POWERED, true);
		builder.part().modelFile(mainTorch4Off).addModel().condition(RedstoneClock.CLOCK_SPEED, 4).condition(RedstoneClock.POWERED, false);
		builder.part().modelFile(mainTorch4On).addModel().condition(RedstoneClock.CLOCK_SPEED, 4).condition(RedstoneClock.POWERED, true);
		builder.part().modelFile(mainTorch5Off).addModel().condition(RedstoneClock.CLOCK_SPEED, 5).condition(RedstoneClock.POWERED, false);
		builder.part().modelFile(mainTorch5On).addModel().condition(RedstoneClock.CLOCK_SPEED, 5).condition(RedstoneClock.POWERED, true);

		simpleItem(b.getRegistryName().getPath());
	}

	private void advancedRedstoneClock(Block b) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(b);

		ModelFile inputTorchOn = getTorchModel("input_torch", true);
		ModelFile inputTorchOff = getTorchModel("input_torch", false);
		ModelFile mainTorch1On = getTorchModel("main_torch_1", true);
		ModelFile mainTorch5Off = getTorchModel("main_torch_5", false);

		ModelFile baseModelOn = models().withExistingParent("block/redstone_clock/advanced_redstone_clock_on", modLoc("block/gate/basic_gate")).texture("top", modLoc("block/advanced_redstone_clock_on")).texture("slab", mcLoc("block/gold_block"));
		ModelFile baseModelOff = models().withExistingParent("block/redstone_clock/advanced_redstone_clock_off", modLoc("block/gate/basic_gate")).texture("top", modLoc("block/advanced_redstone_clock_off")).texture("slab", mcLoc("block/gold_block"));


		builder.part().modelFile(baseModelOn).addModel().condition(AdvancedRedstoneClock.POWERED, true);
		builder.part().modelFile(baseModelOff).addModel().condition(AdvancedRedstoneClock.POWERED, false);

		builder.part().modelFile(mainTorch1On).addModel().condition(AdvancedRedstoneClock.POWERED, true);
		builder.part().modelFile(mainTorch5Off).addModel().condition(AdvancedRedstoneClock.POWERED, false);

		for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
			int yRot = (int) dir.toYRot();
			builder.part().modelFile(inputTorchOn).rotationY(yRot).addModel().condition(AdvancedRedstoneClock.POWERED, true);
			builder.part().modelFile(inputTorchOff).rotationY(yRot).addModel().condition(AdvancedRedstoneClock.POWERED, false);
		}

		simpleItem(b.getRegistryName().getPath());
	}

	private void rsFlipFlop(Block b) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(b);

		ModelFile baseModel = models().withExistingParent("block/rs_flip_flop", modLoc("block/gate/basic_gate")).texture("top", modLoc("block/nor_gate"));
		ModelFile smallRedstoneBlockOn = models().withExistingParent("block/redstone_block/small_redstone_block_on", modLoc("block/redstone_block/small_redstone_block")).texture("block", mcLoc("block/redstone_block"));
		ModelFile smallRedstoneBlockOff = models().withExistingParent("block/redstone_block/small_redstone_block_off", modLoc("block/redstone_block/small_redstone_block")).texture("block", modLoc("block/redstone_block_off"));

		builder.part().modelFile(smallRedstoneBlockOn).addModel().condition(RSFlipFlop.POWERED, true);
		builder.part().modelFile(smallRedstoneBlockOff).addModel().condition(RSFlipFlop.POWERED, false);

		for (Direction dir : RSFlipFlop.FACING.getPossibleValues()) {
			int yRot = (int) dir.toYRot();
			builder.part().modelFile(baseModel).rotationY(yRot).addModel().condition(RSFlipFlop.FACING, dir);
		}

		simpleItem(b.getRegistryName().getPath());
	}

	private void lamp(Block b) {
		String name = b.getRegistryName().getPath();
		VariantBlockStateBuilder builder = getVariantBuilder(b);
		ModelFile powered = models().withExistingParent("block/" + name + "_on", mcLoc("block/cube_all")).texture("all", modLoc("block/" + name + "_on"));
		ModelFile unpowered = models().withExistingParent("block/" + name + "_off", mcLoc("block/cube_all")).texture("all", modLoc("block/" + name + "_off"));
		builder.partialState().with(BlockStateProperties.LIT, true).addModels(new ConfiguredModel(powered));
		builder.partialState().with(BlockStateProperties.LIT, false).addModels(new ConfiguredModel(unpowered));

		simpleBlockItem(b, unpowered);
	}

	private void rotarySwitch(Block b) {
		ModelFile base = models().getExistingFile(modLoc("block/rotary_switch/rotary_switch_base"));
		ModelFile[] models = new ModelFile[16];
		for (int i = 0; i < models.length; i++) {
			models[i] = models().getExistingFile(modLoc("block/rotary_switch/switch_" + i));
		}
		MultiPartBlockStateBuilder builder = getMultipartBuilder(b);

		for (AttachFace face : RotarySwitch.FACE.getPossibleValues()) {
			for (Direction dir : RotarySwitch.FACING.getPossibleValues()) {
				int yRot = (int) dir.toYRot();
				switch (face) {
					case WALL:
						builder.part().modelFile(base).rotationY(yRot).addModel().condition(RotarySwitch.FACE, face).condition(RotarySwitch.FACING, dir);
						break;
					case FLOOR:
						builder.part().modelFile(base).rotationX(90).rotationY(yRot).addModel().condition(RotarySwitch.FACE, face).condition(RotarySwitch.FACING, dir);
						break;
					case CEILING:
						builder.part().modelFile(base).rotationX(270).rotationY(yRot).addModel().condition(RotarySwitch.FACE, face).condition(RotarySwitch.FACING, dir);
						break;
				}
				for (int power : RotarySwitch.POWER.getPossibleValues()) {
					switch (face) {
						case WALL:
							builder.part().modelFile(models[power]).rotationY(yRot).addModel().condition(RotarySwitch.FACE, face).condition(RotarySwitch.FACING, dir).condition(RotarySwitch.POWER, power);
							break;
						case FLOOR:
							builder.part().modelFile(models[power]).rotationX(90).rotationY(yRot).addModel().condition(RotarySwitch.FACE, face).condition(RotarySwitch.FACING, dir).condition(RotarySwitch.POWER, power);
							break;
						case CEILING:
							builder.part().modelFile(models[power]).rotationX(270).rotationY(yRot).addModel().condition(RotarySwitch.FACE, face).condition(RotarySwitch.FACING, dir).condition(RotarySwitch.POWER, power);
							break;
					}
				}
			}
		}
		simpleItem(b.getRegistryName().getPath());
	}

	private void repeater(Block block) {
		String name = block.getRegistryName().getPath();
		String type;

		if (block == GatesBlocks.FAST_REPEATER.get()) {
			type = "fast";
		} else if (block == GatesBlocks.SLOW_REPEATER.get()) {
			type = "slow";
		} else {
			type = "";
		}

		VariantBlockStateBuilder builder = getVariantBuilder(block);

		builder.forAllStates(blockState -> {
			String path = "block/repeater/" + type + "/repeater_" + blockState.getValue(CustomRepeater.DELAY) + "tick";
			if (blockState.getValue(CustomRepeater.POWERED)) {
				path = path + "_on";
			}
			if (blockState.getValue(CustomRepeater.LOCKED)) {
				path = path + "_locked";
			}
			ModelFile model = models().getExistingFile(modLoc(path));

			return ConfiguredModel.builder().modelFile(model).rotationY((int) blockState.getValue(CustomRepeater.FACING).toYRot()).build();
		});

		simpleItem(name);
	}

	private void waxedRedstone(Block b) {
		// FIXME still some rending issues with transparency
		MultiPartBlockStateBuilder builder = getMultipartBuilder(b);

		ModelFile dotModel = models().getExistingFile(modLoc("block/waxed_redstone/waxed_dust_dot"));
		ModelFile lineModel = models().getExistingFile(modLoc("block/waxed_redstone/waxed_dust_side"));
		ModelFile lineModel4 = models().getExistingFile(modLoc("block/waxed_redstone/waxed_dust_4side"));
		ModelFile lineModel3 = models().getExistingFile(modLoc("block/waxed_redstone/waxed_dust_3side"));
		ModelFile lineModel2 = models().getExistingFile(modLoc("block/waxed_redstone/waxed_dust_2side"));
		ModelFile upModel = models().getExistingFile(modLoc("block/waxed_redstone/waxed_dust_side_up"));

		ModelFile line0 = models().withExistingParent("waxed_dust_line0", lineModel.getLocation()).texture("line", mcLoc("block/redstone_dust_line0"));
		ModelFile line1 = models().withExistingParent("waxed_dust_line1", lineModel.getLocation()).texture("line", mcLoc("block/redstone_dust_line1"));

		ModelFile up0 = models().withExistingParent("waxed_dust_line_up0", upModel.getLocation()).texture("line", mcLoc("block/redstone_dust_line0"));
		ModelFile up1 = models().withExistingParent("waxed_dust_line_up1", upModel.getLocation()).texture("line", mcLoc("block/redstone_dust_line1"));


		builder.part().modelFile(dotModel).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.NONE).condition(WaxedRedstone.EAST, RedstoneSide.NONE).condition(WaxedRedstone.SOUTH, RedstoneSide.NONE).condition(WaxedRedstone.WEST, RedstoneSide.NONE);
		builder.part().modelFile(line0).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.EAST, RedstoneSide.NONE).condition(WaxedRedstone.SOUTH, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.WEST, RedstoneSide.NONE);
		builder.part().modelFile(line1).rotationY(90).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.NONE).condition(WaxedRedstone.EAST, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.SOUTH, RedstoneSide.NONE).condition(WaxedRedstone.WEST, RedstoneSide.SIDE, RedstoneSide.UP);
		builder.part().modelFile(lineModel4).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.EAST, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.SOUTH, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.WEST, RedstoneSide.SIDE, RedstoneSide.UP);

		builder.part().modelFile(lineModel2).rotationY(0).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.UP, RedstoneSide.SIDE).condition(WaxedRedstone.EAST, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.SOUTH, RedstoneSide.NONE).condition(WaxedRedstone.WEST, RedstoneSide.NONE);
		builder.part().modelFile(lineModel2).rotationY(90).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.NONE).condition(WaxedRedstone.EAST, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.SOUTH, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.WEST, RedstoneSide.NONE);
		builder.part().modelFile(lineModel2).rotationY(180).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.NONE).condition(WaxedRedstone.EAST, RedstoneSide.NONE).condition(WaxedRedstone.SOUTH, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.WEST, RedstoneSide.SIDE, RedstoneSide.UP);
		builder.part().modelFile(lineModel2).rotationY(270).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.UP, RedstoneSide.SIDE).condition(WaxedRedstone.EAST, RedstoneSide.NONE).condition(WaxedRedstone.SOUTH, RedstoneSide.NONE).condition(WaxedRedstone.WEST, RedstoneSide.SIDE, RedstoneSide.UP);

		builder.part().modelFile(lineModel3).rotationY(0).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.UP, RedstoneSide.SIDE).condition(WaxedRedstone.EAST, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.SOUTH, RedstoneSide.NONE).condition(WaxedRedstone.WEST, RedstoneSide.SIDE, RedstoneSide.UP);
		builder.part().modelFile(lineModel3).rotationY(90).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.EAST, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.SOUTH, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.WEST, RedstoneSide.NONE);
		builder.part().modelFile(lineModel3).rotationY(180).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.NONE).condition(WaxedRedstone.EAST, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.SOUTH, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.WEST, RedstoneSide.SIDE, RedstoneSide.UP);
		builder.part().modelFile(lineModel3).rotationY(270).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.UP, RedstoneSide.SIDE).condition(WaxedRedstone.EAST, RedstoneSide.NONE).condition(WaxedRedstone.SOUTH, RedstoneSide.SIDE, RedstoneSide.UP).condition(WaxedRedstone.WEST, RedstoneSide.SIDE, RedstoneSide.UP);

		builder.part().modelFile(up0).rotationY(0).addModel().condition(WaxedRedstone.NORTH, RedstoneSide.UP);
		builder.part().modelFile(up1).rotationY(90).addModel().condition(WaxedRedstone.EAST, RedstoneSide.UP);
		builder.part().modelFile(up0).rotationY(180).addModel().condition(WaxedRedstone.SOUTH, RedstoneSide.UP);
		builder.part().modelFile(up1).rotationY(270).addModel().condition(WaxedRedstone.WEST, RedstoneSide.UP);

		simpleItem(b.getRegistryName().getPath());
	}

}

