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
		addRedstoneComponent(consumer, GatesBlocks.AND_GATE, Items.IRON_INGOT);
		addRedstoneComponent(consumer, GatesBlocks.OR_GATE, Items.GOLD_INGOT);
		addRedstoneComponent(consumer, GatesBlocks.XOR_GATE, Items.DIAMOND);
		addRedstoneComponent(consumer, GatesBlocks.NAND_GATE, Items.REDSTONE);
		addRedstoneComponent(consumer, GatesBlocks.NOR_GATE, Items.COAL);
		addRedstoneComponent(consumer, GatesBlocks.XNOR_GATE, Items.EMERALD);

		addShaped(consumer, GatesBlocks.NOT_GATE, Items.REDSTONE_TORCH, "   ", "A A", "BBB", i(Items.REDSTONE_TORCH), i(Items.STONE));

		addRedstoneComponent(consumer, GatesBlocks.RS_FLIP_FLOP, GatesBlocks.REDSTONE_BLOCK_OFF);

		addShaped(consumer, GatesBlocks.WIRELESS_REDSTONE_RECEIVER, GatesItems.REDSTONE_TORCH_PEARL, " A ", "ABA", " A ", i(Items.REPEATER), i(GatesItems.REDSTONE_TORCH_PEARL));
		addShaped(consumer, GatesBlocks.WIRELESS_REDSTONE_TRANSMITTER, GatesItems.REDSTONE_TORCH_PEARL, " A ", "ABA", " A ", i(Items.COMPARATOR), i(GatesItems.REDSTONE_TORCH_PEARL));
		addShaped(consumer, GatesBlocks.WIRELESS_REDSTONE_LAMP, GatesItems.REDSTONE_TORCH_PEARL, " A ", "ABA", " A ", i(Items.REDSTONE_LAMP), i(GatesItems.REDSTONE_TORCH_PEARL));

		addShaped(consumer, GatesBlocks.REDSTONE_CLOCK, Items.QUARTZ, " A ", "BCB", "DDD", i(Items.REDSTONE_TORCH), i(Items.REDSTONE), i(Tags.Items.GEMS_QUARTZ), i(Items.STONE));
		addShaped(consumer, GatesBlocks.ADVANCED_REDSTONE_CLOCK, GatesBlocks.REDSTONE_CLOCK, " A ", "ABA", " A ", i(Items.CLOCK), i(GatesBlocks.REDSTONE_CLOCK));

		addShaped(consumer, GatesBlocks.ROTARY_SWITCH, Items.LEVER, " A ", "ABA", " A ", i(Tags.Items.RODS_WOODEN), i(Items.LEVER));

		addShaped(consumer, GatesBlocks.THUNDER_DETECTOR, Items.WATER_BUCKET, "AAA", "BCB", "DDD", i(Tags.Items.GLASS), i(Items.LAPIS_LAZULI), i(Items.WATER_BUCKET), i(ItemTags.WOODEN_SLABS));
		addShaped(consumer, GatesBlocks.RAIN_DETECTOR, Items.LAPIS_LAZULI, "AAA", "BBB", "CCC", i(Items.GLASS), i(Items.LAPIS_LAZULI), i(ItemTags.WOODEN_SLABS));

		addShapeless(consumer, GatesBlocks.FAST_REPEATER, Items.REPEATER, i(Items.REPEATER), i(Items.GOLD_INGOT));
		addShapeless(consumer, GatesBlocks.SLOW_REPEATER, Items.REPEATER, i(Items.REPEATER), i(Items.COAL));
		addShapeless(consumer, GatesItems.FREQUENCY_CHANGER, GatesItems.REDSTONE_TORCH_PEARL, i(Tags.Items.SLIMEBALLS), i(GatesItems.REDSTONE_TORCH_PEARL), i(Items.PAPER));
		addShapeless(consumer, GatesItems.PORTABLE_REDSTONE_TRANSMITTER, GatesItems.REDSTONE_TORCH_PEARL, i(Tags.Items.SLIMEBALLS), i(GatesItems.REDSTONE_TORCH_PEARL), i(ItemTags.BUTTONS));
		addShapeless(consumer, GatesBlocks.REDSTONE_BLOCK_OFF, Items.REDSTONE_BLOCK, i(Items.REDSTONE_BLOCK), i(Items.REDSTONE_TORCH));
		addShapeless(consumer, GatesItems.REDSTONE_TORCH_PEARL, Items.ENDER_PEARL, i(Tags.Items.SLIMEBALLS), i(Items.REDSTONE_TORCH), i(Tags.Items.ENDER_PEARLS));

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
