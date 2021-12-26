package com.github.se7_kn8.gates.data_gen;


import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.GatesItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fmllegacy.RegistryObject;


public class ItemModels extends ItemModelProvider {

	public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Gates.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		addSimpleItemModel(GatesItems.FREQUENCY_CHANGER).transforms().transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).rotation(-45,0,0).scale(0.8f).translation(6,0,-7);
		addSimpleItemModel(GatesItems.PORTABLE_REDSTONE_TRANSMITTER).transforms().transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).rotation(-45,0,0).scale(0.8f).translation(6,0,-7);;
		addSimpleItemModel(GatesItems.REDSTONE_TORCH_PEARL);

	}

	private ItemModelBuilder addSimpleItemModel(RegistryObject<? extends Item> item) {
		String name = item.getId().getPath();
		return withExistingParent(name, mcLoc("item/generated")).texture("layer0", modLoc("item/" + name));
	}

}
