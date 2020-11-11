package com.github.se7_kn8.gates.data_gen;


import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.GatesItems;
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
		addSimpleItemModel(GatesItems.FREQUENCY_CHANGER);
		addSimpleItemModel(GatesItems.PORTABLE_REDSTONE_TRANSMITTER);
		addSimpleItemModel(GatesItems.REDSTONE_TORCH_PEARL);

	}

	private void addSimpleItemModel(IItemProvider item) {
		String name = item.asItem().getRegistryName().getPath();
		withExistingParent(name, mcLoc("item/generated")).texture("layer0", modLoc("item/" + name));
	}

}
