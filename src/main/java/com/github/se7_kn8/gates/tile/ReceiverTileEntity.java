package com.github.se7_kn8.gates.tile;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.api.IWirelessNode;
import com.github.se7_kn8.gates.container.FrequencyMenu;
import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReceiverTileEntity extends BlockEntity implements MenuProvider {

	public interface IWirelessReceiver {
		void onPowerChange(Level level, BlockPos pos, int newPower);
	}

	private String translationKey = "block.gates.receiver";

	public ReceiverTileEntity(BlockPos pos, BlockState state) {
		super(GatesBlocks.RECEIVER_TILE_ENTITY_TYPE.get(), pos, state);
	}

	public void setTranslationKey(String translationKey) {
		this.translationKey = translationKey;
	}

	private LazyOptional<IWirelessNode> wireless = LazyOptional.of(this::createWireless);

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
		if (cap == CapabilityWirelessNode.WIRELESS_NODE) {
			return wireless.cast();
		}
		return super.getCapability(cap);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityWirelessNode.WIRELESS_NODE) {
			return wireless.cast();
		}
		return super.getCapability(cap, side);
	}

	private IWirelessNode createWireless() {
		return new CapabilityWirelessNode.WirelessNodeImpl(1, IWirelessNode.Types.RECEIVER) {
			@Override
			public void setFrequency(int newFrequency) {
				super.setFrequency(newFrequency);
				if (!level.isClientSide) {
					int newPower = RedstoneReceiverWorldSavedData.get((ServerLevel) level).getCurrentFrequencyValue(level, newFrequency);
					setPower(newPower);
				}
				setChanged();
			}

			@Override
			public void setPower(int newPower) {
				super.setPower(newPower);
				Block block = level.getBlockState(getBlockPos()).getBlock();
				if (block instanceof IWirelessReceiver) {
					((IWirelessReceiver) block).onPowerChange(level, getBlockPos(), newPower);
				}
				setChanged();
			}
		};
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		CompoundTag tag = pTag.getCompound("wireless");
		wireless.ifPresent(c -> ((CapabilityWirelessNode.WirelessNodeImpl) c).deserializeNBT(tag));
	}

	@Override
	public CompoundTag save(CompoundTag pTag) {
		wireless.ifPresent(c -> pTag.put("wireless", ((CapabilityWirelessNode.WirelessNodeImpl) c).serializeNBT()));
		return super.save(pTag);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent(translationKey);
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
		return new FrequencyMenu(pContainerId, pInventory, ((CapabilityWirelessNode.WirelessNodeImpl) wireless.orElseGet(this::createWireless)).dataAccess);
	}


}
