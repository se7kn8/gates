package com.github.se7_kn8.gates.tile;

import com.github.se7_kn8.gates.GatesBlocks;
import com.github.se7_kn8.gates.api.CapabilityWirelessNode;
import com.github.se7_kn8.gates.api.IWirelessNode;
import com.github.se7_kn8.gates.block.wireless_redstone.ReceiverBlock;
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ReceiverTileEntity extends TileEntity implements INamedContainerProvider {

	public interface IWirelessReceiver {
		void onPowerChange(World world, BlockPos pos, int newPower);
	}

	private String translationKey = "block.gates.receiver";

	public ReceiverTileEntity() {
		super(GatesBlocks.RECEIVER_TILE_ENTITY_TYPE);
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
				if (!world.isRemote) {
					int newPower = RedstoneReceiverWorldSavedData.get((ServerWorld) world).getCurrentFrequencyValue(world, newFrequency);
					setPower(newPower);
				}
				markDirty();
			}

			@Override
			public void setPower(int newPower) {
				super.setPower(newPower);
				Block block = world.getBlockState(pos).getBlock();
				if (block instanceof IWirelessReceiver) {
					((IWirelessReceiver) block).onPowerChange(world, pos, newPower);
				}
				markDirty();
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
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		CompoundNBT wirelessTag = compound.getCompound("wireless");
		wireless.ifPresent(c -> ((INBTSerializable<CompoundNBT>) c).deserializeNBT(wirelessTag));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		wireless.ifPresent(c -> {
			CompoundNBT compoundNBT = ((INBTSerializable<CompoundNBT>) c).serializeNBT();
			compound.put("wireless", compoundNBT);
		});
		return super.write(compound);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(translationKey);
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
		return new FrequencyContainer(windowId, world, pos, inventory, player);
	}
}
