package com.github.se7_kn8.gates.data_gen;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.GatesItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

	public Recipes(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
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

		addShaped(consumer, GatesBlocks.IRON_FENCE.get(), 27, Items.IRON_INGOT, "   ", "ABA", "ABA", i(Tags.Items.STORAGE_BLOCKS_IRON), i(Tags.Items.INGOTS_IRON));
		addShaped(consumer, GatesBlocks.IRON_FENCE_GATE.get(), 9, Items.IRON_INGOT, "   ", "BAB", "BAB", i(Tags.Items.STORAGE_BLOCKS_IRON), i(Tags.Items.INGOTS_IRON));

		addShapeless(consumer, GatesBlocks.FAST_REPEATER.get(), Items.REPEATER, i(Items.REPEATER), i(Items.GOLD_INGOT));
		addShapeless(consumer, GatesBlocks.SLOW_REPEATER.get(), Items.REPEATER, i(Items.REPEATER), i(Items.COAL));
		addShapeless(consumer, GatesItems.FREQUENCY_CHANGER.get(), GatesItems.REDSTONE_TORCH_PEARL.get(), i(Tags.Items.SLIMEBALLS), i(GatesItems.REDSTONE_TORCH_PEARL.get()), i(Items.PAPER));
		addShapeless(consumer, GatesItems.PORTABLE_REDSTONE_TRANSMITTER.get(), GatesItems.REDSTONE_TORCH_PEARL.get(), i(Tags.Items.SLIMEBALLS), i(GatesItems.REDSTONE_TORCH_PEARL.get()), i(ItemTags.BUTTONS));
		addShapeless(consumer, GatesBlocks.REDSTONE_BLOCK_OFF.get(), Items.REDSTONE_BLOCK, i(Items.REDSTONE_BLOCK), i(Items.REDSTONE_TORCH));
		addShapeless(consumer, GatesItems.REDSTONE_TORCH_PEARL.get(), Items.ENDER_PEARL, i(Tags.Items.SLIMEBALLS), i(Items.REDSTONE_TORCH), i(Tags.Items.ENDER_PEARLS));
		addShapeless(consumer, GatesBlocks.WAXED_REDSTONE_WIRE.get(), Items.HONEY_BOTTLE, i(Tags.Items.DUSTS_REDSTONE), i(Items.HONEY_BOTTLE));
	}

	private void addRedstoneComponent(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike component) {
		addShaped(consumer, output, component, " A ", "ABA", "CCC", i(Items.REDSTONE_TORCH), i(component), i(Items.STONE));
	}

	private void addShaped(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike requirement, String p1, String p2, String p3, Ingredient... ingredients) {
		addShaped(consumer, output, 1,requirement, p1, p2, p3, ingredients);
	}
	private void addShaped(Consumer<FinishedRecipe> consumer, ItemLike output, int outputCount, ItemLike requirement, String p1, String p2, String p3, Ingredient... ingredients) {
		ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(output, outputCount)
				.pattern(p1)
				.pattern(p2)
				.pattern(p3)
				.unlockedBy(genCriterionName(requirement), has(requirement));

		int counter = 0;
		String recipeChars = "ABCDEFGHI";
		for (Ingredient ingredient : ingredients) {
			builder.define(recipeChars.charAt(counter), ingredient);
			counter++;
		}
		builder.save(consumer);
	}

	private void addShapeless(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike requirement, Ingredient... ingredients) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(output, 1)
				.unlockedBy(genCriterionName(requirement), has(requirement));

		for (Ingredient ingredient : ingredients) {
			builder.requires(ingredient);
		}

		builder.save(consumer);
	}


	private String genCriterionName(ItemLike input) {
		return "has_" + input.asItem();
	}

	private Ingredient i(ItemLike input) {
		return Ingredient.of(input);
	}

	private Ingredient i(TagKey<Item> input) {
		return Ingredient.of(input);
	}

	private Ingredient i(ItemStack stack) {
		return Ingredient.of(stack);
	}

}
