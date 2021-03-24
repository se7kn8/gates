package com.github.se7_kn8.gates.tile;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.api.IWirelessNode;
import com.github.se7_kn8.gates.container.FrequencyContainer;
import com.github.se7_kn8.gates.data.RedstoneReceiverWorldSavedData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReceiverTileEntity extends TileEntity implements INamedContainerProvider {

	public interface IWirelessReceiver {
		void onPowerChange(World world, BlockPos pos, int newPower);
	}

	private String translationKey = "block.gates.receiver";

	public ReceiverTileEntity() {
		super(GatesBlocks.RECEIVER_TILE_ENTITY_TYPE.get());
	}

	public void setTranslationKey(String translationKey) {
		this.translationKey = translationKey;
	}

	private LazyOptional<IWirelessNode> wireless = LazyOptional.of(this::createWireless);

	private IWirelessNode createWireless() {
		return new CapabilityWirelessNode.WirelessNodeImpl(1, IWirelessNode.Types.RECEIVER) {
			@Override
			public void setFrequency(int newFrequency) {
				super.setFrequency(newFrequency);
				if (!level.isClientSide) {
					int newPower = RedstoneReceiverWorldSavedData.get((ServerWorld) level).getCurrentFrequencyValue(level, newFrequency);
					setPower(newPower);
				}
				setChanged();
			}

			@Override
			public void setPower(int newPower) {
				super.setPower(newPower);
				Block block = level.getBlockState(worldPosition).getBlock();
				if (block instanceof IWirelessReceiver) {
					((IWirelessReceiver) block).onPowerChange(level, worldPosition, newPower);
				}
				setChanged();
			}
		};
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityWirelessNode.WIRELESS_NODE) {
			return wireless.cast();
		}
		return super.getCapability(cap, side);
	}


	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		CompoundNBT wirelessTag = compound.getCompound("wireless");
		wireless.ifPresent(c -> ((INBTSerializable<CompoundNBT>) c).deserializeNBT(wirelessTag));
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		wireless.ifPresent(c -> {
			CompoundNBT compoundNBT = ((INBTSerializable<CompoundNBT>) c).serializeNBT();
			compound.put("wireless", compoundNBT);
		});
		return super.save(compound);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(translationKey);
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
		return new FrequencyContainer(windowId, level, worldPosition, inventory, player);
	}
}
