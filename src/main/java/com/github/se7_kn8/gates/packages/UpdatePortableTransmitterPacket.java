package com.github.se7_kn8.gates.packages;

import com.github.se7_kn8.gates.GatesItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdatePortableTransmitterPacket implements BasePacket {

	private final int newFrequency;
	private final int hand;

	public UpdatePortableTransmitterPacket(int newFrequency, Hand hand) {
		this.newFrequency = newFrequency;
		this.hand = hand.ordinal();
	}

	public UpdatePortableTransmitterPacket(PacketBuffer buffer) {
		this.newFrequency = buffer.readInt();
		this.hand = buffer.readInt();
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeInt(newFrequency);
		buffer.writeInt(hand);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ItemStack stack = ctx.get().getSender().getHeldItem(Hand.values()[hand]);
			if (stack.getItem() == GatesItems.PORTABLE_REDSTONE_TRANSMITTER) {
				if (!stack.hasTag()) {
					stack.setTag(new CompoundNBT());
				}
				stack.getTag().putInt("frequency", newFrequency);
			}
		});
	}

}
