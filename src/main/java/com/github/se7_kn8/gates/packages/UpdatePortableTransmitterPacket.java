package com.github.se7_kn8.gates.packages;

import com.github.se7_kn8.gates.GatesItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdatePortableTransmitterPacket implements BasePacket {

	private final int newFrequency;
	private final int hand;

	public UpdatePortableTransmitterPacket(int newFrequency, InteractionHand hand) {
		this.newFrequency = newFrequency;
		this.hand = hand.ordinal();
	}

	public UpdatePortableTransmitterPacket(FriendlyByteBuf buffer) {
		this.newFrequency = buffer.readInt();
		this.hand = buffer.readInt();
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(newFrequency);
		buffer.writeInt(hand);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ItemStack stack = ctx.get().getSender().getItemInHand(InteractionHand.values()[hand]);
			if (stack.getItem() == GatesItems.PORTABLE_REDSTONE_TRANSMITTER.get()) {
				if (!stack.hasTag()) {
					stack.setTag(new CompoundTag());
				}
				stack.getTag().putInt("frequency", newFrequency);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
