package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.GatesBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTags extends BlockTagsProvider {

	public BlockTags(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
		super(pGenerator, Gates.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(net.minecraft.tags.BlockTags.FENCES).add(GatesBlocks.IRON_FENCE.get());
		tag(net.minecraft.tags.BlockTags.FENCE_GATES).add(GatesBlocks.IRON_FENCE_GATE.get());

		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
				.add(GatesBlocks.REDSTONE_BLOCK_OFF.get())
				.add(GatesBlocks.IRON_FENCE.get())
				.add(GatesBlocks.IRON_FENCE_GATE.get());

		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE)
				.add(GatesBlocks.RAIN_DETECTOR.get())
				.add(GatesBlocks.THUNDER_DETECTOR.get());
	}
}
