package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.GatesItems;
import com.github.se7_kn8.gates.PacketHandler;
import com.github.se7_kn8.gates.container.PortableRedstoneTransmitterContainer;
import com.github.se7_kn8.gates.packages.UpdatePortableTransmitterPacket;
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

public class PortableTransmitterScreen extends ContainerScreen<PortableRedstoneTransmitterContainer> {

	private TextFieldWidget frequencyField;
	private Button applyButton;

	public PortableTransmitterScreen(PortableRedstoneTransmitterContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();

		frequencyField = new TextFieldWidget(this.font, this.width / 2 - 35, this.height / 2 - 50, 70, 20, I18n.format("gates.gui.transmitter"));
		frequencyField.setValidator(s -> (s.matches("^[0-9]+$") || s.equals("")) && s.length() < 10);
		this.children.add(frequencyField);

		applyButton = new Button(this.width / 2 - 40, this.height / 2, 80, 20, I18n.format("gui.gates.apply"), button -> {
			if (!frequencyField.getText().trim().equals("")) {
				Hand hand = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() == GatesItems.PORTABLE_REDSTONE_TRANSMITTER ? Hand.MAIN_HAND : Hand.OFF_HAND;
				PacketHandler.MOD_CHANNEL.sendToServer(new UpdatePortableTransmitterPacket(Integer.parseInt(frequencyField.getText()), hand));
				Minecraft.getInstance().player.closeScreen();
			}
		});

		this.setFocusedDefault(frequencyField);

		this.addButton(applyButton);

	}

	@Override
	public void tick() {
		this.frequencyField.tick();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		this.frequencyField.render(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.renderBackground();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(this.title.getFormattedText(), 8.0F, 4.0F, 14737632);
		this.font.drawString(new TranslationTextComponent("gui.gates.frequency").getFormattedText(), 40, 20, 14737632);
	}
}
