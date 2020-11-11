package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.Gates;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModels extends BlockModelProvider {

	public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Gates.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		// not needed atm
	}

}
