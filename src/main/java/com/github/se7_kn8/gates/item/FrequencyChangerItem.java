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
		super(new Item.Properties().group(Gates.GATES_ITEM_GROUP).maxStackSize(1).rarity(Rarity.UNCOMMON));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		if (stack.hasTag()) {
			if (stack.getTag().contains("frequency")) {
				tooltip.add(new TranslationTextComponent("gui.gates.current_frequency_stored", stack.getTag().getInt("frequency")));
			}
		} else {
			tooltip.add(new TranslationTextComponent("gui.gates.usage.frequency_changer"));
		}
	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn.isSneaking()) {
			stack.setTag(new CompoundNBT());
			if (worldIn.isRemote) {
				playerIn.sendStatusMessage(new TranslationTextComponent("gui.gates.frequency_cleared"), true);
			}
			return ActionResult.resultSuccess(stack);
		}

		return ActionResult.resultPass(stack);
	}

	@Override
	@Nonnull
	public ActionResultType onItemUse(ItemUseContext ctx) {
		ItemStack stack = ctx.getItem();


		if (!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
		}

		CompoundNBT stackNBT = stack.getTag();

		if (ctx.hasSecondaryUseForPlayer()) {
			if (CapabilityUtil.findWirelessCapability(ctx.getWorld(), ctx.getPos(), c -> stackNBT.putInt("frequency", c.getFrequency()))) {
				if (ctx.getWorld().isRemote) {
					ctx.getPlayer().sendStatusMessage(new TranslationTextComponent("gui.gates.frequency_saved"), true);
				}
				return ActionResultType.SUCCESS;
			}
		} else {
			if (stackNBT.contains("frequency")) {
				if (CapabilityUtil.findWirelessCapability(ctx.getWorld(), ctx.getPos(), c -> c.setFrequency(stackNBT.getInt("frequency")))) {
					if (ctx.getWorld().isRemote) {
						ctx.getPlayer().sendStatusMessage(new TranslationTextComponent("gui.gates.frequency_set"), true);
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
