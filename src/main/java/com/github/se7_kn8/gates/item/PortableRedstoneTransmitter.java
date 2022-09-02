package com.github.se7_kn8.gates.item;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import com.github.se7_kn8.gates.menu.PortableRedstoneTransmitterMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class PortableRedstoneTransmitter extends Item {

	public PortableRedstoneTransmitter() {
		super(new Item.Properties().tab(Gates.GATES_ITEM_GROUP).stacksTo(1).rarity(Rarity.UNCOMMON));
	}


	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
		pTooltipComponents.add(Component.translatable("gui.gates.usage.portable_transmitter_1"));
		pTooltipComponents.add(Component.translatable("gui.gates.usage.portable_transmitter_2"));
		pTooltipComponents.add(Component.translatable("gui.gates.usage.portable_transmitter_3"));
		if (pStack.hasTag()) {
			if (pStack.getTag().contains("frequency")) {
				pTooltipComponents.add(Component.translatable("gui.gates.current_frequency_stored", pStack.getTag().getInt("frequency")));
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);

		if (pPlayer.isShiftKeyDown()) {
			if (!pLevel.isClientSide) {
				pPlayer.openMenu(new MenuProvider() {
					@Nullable
					@Override
					public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
						return new PortableRedstoneTransmitterMenu(pContainerId);
					}

					@Override
					public Component getDisplayName() {
						return Component.translatable("item.gates.portable_redstone_transmitter");
					}
				});
			}
			return InteractionResultHolder.success(stack);
		}

		if (!stack.hasTag()) {
			stack.setTag(new CompoundTag());
		}

		if (!stack.getTag().contains("frequency")) {
			pPlayer.displayClientMessage(Component.translatable("gui.gates.no_frequency"), true);
			return InteractionResultHolder.fail(stack);
		}

		int frequency = stack.getTag().getInt("frequency");

		if (stack.getTag().contains("active")) {
			boolean active = !stack.getTag().getBoolean("active");
			stack.getTag().putBoolean("active", active);
			update(pLevel, active, frequency);
		} else {
			stack.getTag().putBoolean("active", true);
			update(pLevel, true, frequency);
		}

		return InteractionResultHolder.success(stack);
	}


	private void update(Level level, boolean active, int frequency) {
		if (!level.isClientSide) {
			RedstoneReceiverWorldSavedData.get((ServerLevel) level).updateFrequency(level, frequency, active ? 15 : 0);
		}
	}


	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.hasTag() && stack.getTag().contains("active") && stack.getTag().getBoolean("active");
	}
}
