package com.github.se7_kn8.gates.item;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.api.CapabilityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

import javax.annotation.Nullable;
import java.util.List;

public class FrequencyChangerItem extends Item {


	public FrequencyChangerItem() {
		super(new Item.Properties().tab(Gates.GATES_ITEM_GROUP).stacksTo(1).rarity(Rarity.UNCOMMON));
	}


	@Override
	public void appendHoverText(ItemStack pStack, @Nullable net.minecraft.world.level.Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
		if (pStack.hasTag()) {
			if (pStack.getTag().contains("frequency")) {
				pTooltipComponents.add(new TranslatableComponent("gui.gates.current_frequency_stored", pStack.getTag().getInt("frequency")));
			}
		} else {
			pTooltipComponents.add(new TranslatableComponent("gui.gates.usage.frequency_changer_1"));
			pTooltipComponents.add(new TranslatableComponent("gui.gates.usage.frequency_changer_2"));
			pTooltipComponents.add(new TranslatableComponent("gui.gates.usage.frequency_changer_3"));
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);

		if (pPlayer.isShiftKeyDown()) {
			stack.setTag(new CompoundTag());
			if (pLevel.isClientSide) {
				pPlayer.displayClientMessage(new TranslatableComponent("gui.gates.frequency_cleared"), true);
			}
			return InteractionResultHolder.success(stack);
		}

		return InteractionResultHolder.pass(stack);
	}


	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		ItemStack stack = pContext.getItemInHand();


		if (!stack.hasTag()) {
			stack.setTag(new CompoundTag());
		}

		CompoundTag stackNBT = stack.getTag();

		if (pContext.isSecondaryUseActive()) {
			if (CapabilityUtil.findWirelessCapability(pContext.getLevel(), pContext.getClickedPos(), c -> stackNBT.putInt("frequency", c.getFrequency()))) {
				if (pContext.getLevel().isClientSide) {
					pContext.getPlayer().displayClientMessage(new TranslatableComponent("gui.gates.frequency_saved"), true);
				}
				return InteractionResult.SUCCESS;
			}
		} else {
			if (stackNBT.contains("frequency")) {
				if (CapabilityUtil.findWirelessCapability(pContext.getLevel(), pContext.getClickedPos(), c -> c.setFrequency(stackNBT.getInt("frequency")))) {
					if (pContext.getLevel().isClientSide) {
						pContext.getPlayer().displayClientMessage(new TranslatableComponent("gui.gates.frequency_set"), true);
					}
					return InteractionResult.SUCCESS;
				}
			}
		}

		return InteractionResult.FAIL;
	}

	@Override
	public boolean doesSneakBypassUse(ItemStack stack, LevelReader world, BlockPos pos, Player player) {
		return false;
	}
}
