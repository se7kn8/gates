package com.github.se7_kn8.gates.item;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.container.PortableRedstoneTransmitterContainer;
import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class PortableRedstoneTransmitter extends Item {

	public PortableRedstoneTransmitter() {
		super(new Item.Properties().group(Gates.GATES_ITEM_GROUP).maxStackSize(1).rarity(Rarity.UNCOMMON));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("gui.gates.usage.portable_transmitter"));
		if (stack.hasTag()) {
			if (stack.getTag().contains("frequency")) {
				tooltip.add(new TranslationTextComponent("gui.gates.current_frequency_stored", stack.getTag().getInt("frequency")));
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn.isSneaking()) {
			if (!worldIn.isRemote) {
				playerIn.openContainer(new INamedContainerProvider() {
					@Override
					public ITextComponent getDisplayName() {
						return new TranslationTextComponent("item.gates.portable_redstone_transmitter");
					}

					@Nullable
					@Override
					public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
						return new PortableRedstoneTransmitterContainer(p_createMenu_1_);
					}
				});
			}
			return ActionResult.resultSuccess(stack);
		}

		if (!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
		}

		if (!stack.getTag().contains("frequency")) {
			playerIn.sendStatusMessage(new TranslationTextComponent("gui.gates.no_frequency"), true);
			return ActionResult.resultFail(stack);
		}

		int frequency = stack.getTag().getInt("frequency");

		if (stack.getTag().contains("active"))  {
			boolean active = !stack.getTag().getBoolean("active");
			stack.getTag().putBoolean("active", active);
			update(worldIn, active, frequency);
		} else {
			stack.getTag().putBoolean("active", true);
			update(worldIn, true, frequency);
		}

		return ActionResult.resultSuccess(stack);
	}

	private void update(World world, boolean active, int frequency) {
		if (!world.isRemote) {
			RedstoneReceiverWorldSavedData.get((ServerWorld) world).updateFrequency(world, frequency, active ? 15 : 0);
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.hasTag() && stack.getTag().contains("active") && stack.getTag().getBoolean("active");
	}


}
