package com.github.se7_kn8.gates.block.entity;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.api.IWirelessNode;
import com.github.se7_kn8.gates.block.wireless_redstone.TransmitterBlock;
import com.github.se7_kn8.gates.container.FrequencyMenu;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TransmitterBlockEntity extends BlockEntity implements MenuProvider {


	public TransmitterBlockEntity(BlockPos pos, BlockState state) {
		super(GatesBlocks.TRANSMITTER_BLOCK_ENTITY_TYPE.get(), pos, state);
	}

	private final LazyOptional<IWirelessNode> wireless = LazyOptional.of(this::createWireless);

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

	@Nonnull
	private IWirelessNode createWireless() {
		return new CapabilityWirelessNode.WirelessNodeImpl(1, IWirelessNode.Types.TRANSMITTER) {
			@Override
			public void setPower(int newPower) {
				super.setPower(newPower);
				setChanged();
			}

			@Override
			public void setFrequency(int newFrequency) {
				int oldFrequency = this.getFrequency();
				super.setFrequency(newFrequency);

				if (!level.isClientSide) {
					if (level.getBlockState(getBlockPos()).getBlock() instanceof TransmitterBlock block) {
						block.updateFrequency((ServerLevel) level, getBlockPos(), oldFrequency);
					}
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
	protected void saveAdditional(CompoundTag pTag) {
		wireless.ifPresent(c -> pTag.put("wireless", ((CapabilityWirelessNode.WirelessNodeImpl) c).serializeNBT()));
		super.saveAdditional(pTag);
	}

	@Override
	@Nonnull
	public Component getDisplayName() {
		return new TranslatableComponent("block.gates.transmitter");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
		return new FrequencyMenu(pContainerId, pInventory, ((CapabilityWirelessNode.WirelessNodeImpl) wireless.orElseGet(this::createWireless)).dataAccess);
	}

}
