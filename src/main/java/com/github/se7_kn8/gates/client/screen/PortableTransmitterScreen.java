package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.GatesItems;
import com.github.se7_kn8.gates.PacketHandler;
import com.github.se7_kn8.gates.menu.PortableRedstoneTransmitterMenu;
import com.github.se7_kn8.gates.packages.UpdatePortableTransmitterPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;

import javax.annotation.Nonnull;

public class PortableTransmitterScreen extends AbstractContainerScreen<PortableRedstoneTransmitterMenu> {

	private EditBox frequencyField;

	public PortableTransmitterScreen(PortableRedstoneTransmitterMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();

		frequencyField = new EditBox(this.font, this.width / 2 - 35, this.height / 2 - 50, 70, 20, Component.translatable("gates.gui.transmitter"));
		frequencyField.setFilter(s -> (s.matches("^[0-9]+$") || s.equals("")) && s.length() < 10);

		this.addRenderableWidget(frequencyField);

		this.addRenderableWidget(new Button(this.width / 2 - 40, this.height / 2, 80, 20, Component.translatable("gui.gates.apply"), button -> {
			if (!frequencyField.getValue().trim().equals("")) {
				Item item = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
				InteractionHand hand = item == GatesItems.PORTABLE_REDSTONE_TRANSMITTER.get() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
				PacketHandler.MOD_CHANNEL.sendToServer(new UpdatePortableTransmitterPacket(Integer.parseInt(frequencyField.getValue()), hand));
				Minecraft.getInstance().player.clientSideCloseContainer();
			}
		}));

		this.setInitialFocus(frequencyField);

	}

	@Override
	public void containerTick() {
		super.containerTick();
		this.frequencyField.tick();
	}

	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		this.renderBackground(pPoseStack);
		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	}

	@Override
	protected void renderBg(@Nonnull PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
		this.font.draw(pPoseStack, Component.translatable("gui.gates.frequency"), 40, 20, 0xFFFFFFFF);
	}
}
