package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.GatesItems;
import com.github.se7_kn8.gates.PacketHandler;
import com.github.se7_kn8.gates.container.PortableRedstoneTransmitterContainer;
import com.github.se7_kn8.gates.packages.UpdatePortableTransmitterPacket;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class PortableTransmitterScreen extends ContainerScreen<PortableRedstoneTransmitterContainer> {

	private TextFieldWidget frequencyField;

	public PortableTransmitterScreen(PortableRedstoneTransmitterContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();

		frequencyField = new TextFieldWidget(this.font, this.width / 2 - 35, this.height / 2 - 50, 70, 20, new TranslationTextComponent("gates.gui.transmitter"));
		frequencyField.setValidator(s -> (s.matches("^[0-9]+$") || s.equals("")) && s.length() < 10);

		this.children.add(frequencyField);

		this.addButton(new Button(this.width / 2 - 40, this.height / 2, 80, 20, new TranslationTextComponent("gui.gates.apply"), button -> {
			if (!frequencyField.getText().trim().equals("")) {
				Hand hand = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() == GatesItems.PORTABLE_REDSTONE_TRANSMITTER ? Hand.MAIN_HAND : Hand.OFF_HAND;
				PacketHandler.MOD_CHANNEL.sendToServer(new UpdatePortableTransmitterPacket(Integer.parseInt(frequencyField.getText()), hand));
				Minecraft.getInstance().player.closeScreen();
			}
		}));

		this.setFocusedDefault(frequencyField);

	}

	@Override
	public void tick() {
		super.tick();
		this.frequencyField.tick();
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);

		this.frequencyField.render(stack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack stack, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);
		// mappings: drawString
		this.font.func_243248_b(stack, new TranslationTextComponent("gui.gates.frequency"), 40, 20, 14737632);
	}
}
