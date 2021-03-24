package com.github.se7_kn8.gates.item;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.api.CapabilityUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FrequencyChangerItem extends Item {


	public FrequencyChangerItem() {
		super(new Item.Properties().tab(Gates.GATES_ITEM_GROUP).stacksTo(1).rarity(Rarity.UNCOMMON));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);
		if (stack.hasTag()) {
			if (stack.getTag().contains("frequency")) {
				tooltip.add(new TranslationTextComponent("gui.gates.current_frequency_stored", stack.getTag().getInt("frequency")));
			}
		} else {
			tooltip.add(new TranslationTextComponent("gui.gates.usage.frequency_changer_1"));
			tooltip.add(new TranslationTextComponent("gui.gates.usage.frequency_changer_2"));
			tooltip.add(new TranslationTextComponent("gui.gates.usage.frequency_changer_3"));
		}
	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn.isShiftKeyDown()) {
			stack.setTag(new CompoundNBT());
			if (worldIn.isClientSide) {
				playerIn.displayClientMessage(new TranslationTextComponent("gui.gates.frequency_cleared"), true);
			}
			return ActionResult.success(stack);
		}

		return ActionResult.pass(stack);
	}

	@Override
	@Nonnull
	public ActionResultType useOn(ItemUseContext ctx) {
		ItemStack stack = ctx.getItemInHand();


		if (!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
		}

		CompoundNBT stackNBT = stack.getTag();

		if (ctx.isSecondaryUseActive()) {
			if (CapabilityUtil.findWirelessCapability(ctx.getLevel(), ctx.getClickedPos(), c -> stackNBT.putInt("frequency", c.getFrequency()))) {
				if (ctx.getLevel().isClientSide) {
					ctx.getPlayer().displayClientMessage(new TranslationTextComponent("gui.gates.frequency_saved"), true);
				}
				return ActionResultType.SUCCESS;
			}
		} else {
			if (stackNBT.contains("frequency")) {
				if (CapabilityUtil.findWirelessCapability(ctx.getLevel(), ctx.getClickedPos(), c -> c.setFrequency(stackNBT.getInt("frequency")))) {
					if (ctx.getLevel().isClientSide) {
						ctx.getPlayer().displayClientMessage(new TranslationTextComponent("gui.gates.frequency_set"), true);
					}
					return ActionResultType.SUCCESS;
				}
			}
		}

		return ActionResultType.FAIL;
	}


	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
		return false;
	}
}
