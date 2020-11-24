package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.GatesItems;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

	public Recipes(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
		addRedstoneComponent(consumer, GatesBlocks.AND_GATE.get(), Items.IRON_INGOT);
		addRedstoneComponent(consumer, GatesBlocks.OR_GATE.get(), Items.GOLD_INGOT);
		addRedstoneComponent(consumer, GatesBlocks.XOR_GATE.get(), Items.DIAMOND);
		addRedstoneComponent(consumer, GatesBlocks.NAND_GATE.get(), Items.REDSTONE);
		addRedstoneComponent(consumer, GatesBlocks.NOR_GATE.get(), Items.COAL);
		addRedstoneComponent(consumer, GatesBlocks.XNOR_GATE.get(), Items.EMERALD);

		addShaped(consumer, GatesBlocks.NOT_GATE.get(), Items.REDSTONE_TORCH, "   ", "A A", "BBB", i(Items.REDSTONE_TORCH), i(Items.STONE));

		addRedstoneComponent(consumer, GatesBlocks.RS_FLIP_FLOP.get(), GatesBlocks.REDSTONE_BLOCK_OFF.get());

		addShaped(consumer, GatesBlocks.WIRELESS_REDSTONE_RECEIVER.get(), GatesItems.REDSTONE_TORCH_PEARL.get(), " A ", "ABA", " A ", i(Items.REPEATER), i(GatesItems.REDSTONE_TORCH_PEARL.get()));
		addShaped(consumer, GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER.get(), GatesItems.REDSTONE_TORCH_PEARL.get(), " A ", "ABA", " A ", i(Items.COMPARATOR), i(GatesItems.REDSTONE_TORCH_PEARL.get()));
		addShaped(consumer, GatesBlocks.WIRELESS_REDSTONE_LAMP.get(), GatesItems.REDSTONE_TORCH_PEARL.get(), " A ", "ABA", " A ", i(Items.REDSTONE_LAMP), i(GatesItems.REDSTONE_TORCH_PEARL.get()));

		addShaped(consumer, GatesBlocks.REDSTONE_CLOCK.get(), Items.QUARTZ, " A ", "BCB", "DDD", i(Items.REDSTONE_TORCH), i(Items.REDSTONE), i(Tags.Items.GEMS_QUARTZ), i(Items.STONE));
		addShaped(consumer, GatesBlocks.ADVANCED_REDSTONE_CLOCK.get(), GatesBlocks.REDSTONE_CLOCK.get(), " A ", "ABA", " A ", i(Items.CLOCK), i(GatesBlocks.REDSTONE_CLOCK.get()));

		addShaped(consumer, GatesBlocks.ROTARY_SWITCH.get(), Items.LEVER, " A ", "ABA", " A ", i(Tags.Items.RODS_WOODEN), i(Items.LEVER));

		addShaped(consumer, GatesBlocks.THUNDER_DETECTOR.get(), Items.WATER_BUCKET, "AAA", "BCB", "DDD", i(Tags.Items.GLASS), i(Items.LAPIS_LAZULI), i(Items.WATER_BUCKET), i(ItemTags.WOODEN_SLABS));
		addShaped(consumer, GatesBlocks.RAIN_DETECTOR.get(), Items.LAPIS_LAZULI, "AAA", "BBB", "CCC", i(Items.GLASS), i(Items.LAPIS_LAZULI), i(ItemTags.WOODEN_SLABS));

		addShapeless(consumer, GatesBlocks.FAST_REPEATER.get(), Items.REPEATER, i(Items.REPEATER), i(Items.GOLD_INGOT));
		addShapeless(consumer, GatesBlocks.SLOW_REPEATER.get(), Items.REPEATER, i(Items.REPEATER), i(Items.COAL));
		addShapeless(consumer, GatesItems.FREQUENCY_CHANGER.get(), GatesItems.REDSTONE_TORCH_PEARL.get(), i(Tags.Items.SLIMEBALLS), i(GatesItems.REDSTONE_TORCH_PEARL.get()), i(Items.PAPER));
		addShapeless(consumer, GatesItems.PORTABLE_REDSTONE_TRANSMITTER.get(), GatesItems.REDSTONE_TORCH_PEARL.get(), i(Tags.Items.SLIMEBALLS), i(GatesItems.REDSTONE_TORCH_PEARL.get()), i(ItemTags.BUTTONS));
		addShapeless(consumer, GatesBlocks.REDSTONE_BLOCK_OFF.get(), Items.REDSTONE_BLOCK, i(Items.REDSTONE_BLOCK), i(Items.REDSTONE_TORCH));
		addShapeless(consumer, GatesItems.REDSTONE_TORCH_PEARL.get(), Items.ENDER_PEARL, i(Tags.Items.SLIMEBALLS), i(Items.REDSTONE_TORCH), i(Tags.Items.ENDER_PEARLS));
		addShapeless(consumer, GatesBlocks.WAXED_REDSTONE_WIRE.get(), Items.HONEY_BOTTLE, i(Tags.Items.DUSTS_REDSTONE), i(Items.HONEY_BOTTLE));
	}

	private void addRedstoneComponent(Consumer<IFinishedRecipe> consumer, IItemProvider output, IItemProvider component) {
		addShaped(consumer, output, component, " A ", "ABA", "CCC", i(Items.REDSTONE_TORCH), i(component), i(Items.STONE));
	}

	private void addShaped(Consumer<IFinishedRecipe> consumer, IItemProvider output, IItemProvider requirement, String p1, String p2, String p3, Ingredient... ingredients) {
		ShapedRecipeBuilder builder = ShapedRecipeBuilder.shapedRecipe(output, 1)
				.patternLine(p1)
				.patternLine(p2)
				.patternLine(p3)
				.addCriterion(genCriterionName(requirement), hasItem(requirement));

		int counter = 0;
		String recipeChars = "ABCDEFGHI";
		for (Ingredient ingredient : ingredients) {
			builder.key(recipeChars.charAt(counter), ingredient);
			counter++;
		}
		builder.build(consumer);
	}

	private void addShapeless(Consumer<IFinishedRecipe> consumer, IItemProvider output, IItemProvider requirement, Ingredient... ingredients) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapelessRecipe(output, 1)
				.addCriterion(genCriterionName(requirement), hasItem(requirement));

		for (Ingredient ingredient : ingredients) {
			builder.addIngredient(ingredient);
		}

		builder.build(consumer);
	}


	private String genCriterionName(IItemProvider input) {
		return "has_" + input.asItem().getRegistryName().getPath();
	}

	private Ingredient i(IItemProvider input) {
		return Ingredient.fromItems(input);
	}

	private Ingredient i(ITag<Item> input) {
		return Ingredient.fromTag(input);
	}

	private Ingredient i(ItemStack stack) {
		return Ingredient.fromStacks(stack);
	}

}
