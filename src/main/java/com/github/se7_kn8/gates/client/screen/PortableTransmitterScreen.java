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
	// mappings: init
	protected void func_231160_c_() {
		super.func_231160_c_();

		// mappings: font, width, height
		frequencyField = new TextFieldWidget(this.field_230712_o_, this.field_230708_k_ / 2 - 35, this.field_230709_l_ / 2 - 50, 70, 20, new TranslationTextComponent("gates.gui.transmitter"));
		frequencyField.setValidator(s -> (s.matches("^[0-9]+$") || s.equals("")) && s.length() < 10);

		// mappings: children
		this.field_230705_e_.add(frequencyField);

		// mappings: addButton, width, height
		this.func_230480_a_(new Button(this.field_230708_k_ / 2 - 40, this.field_230709_l_ / 2, 80, 20, new TranslationTextComponent("gui.gates.apply"), button -> {
			if (!frequencyField.getText().trim().equals("")) {
				Hand hand = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() == GatesItems.PORTABLE_REDSTONE_TRANSMITTER ? Hand.MAIN_HAND : Hand.OFF_HAND;
				PacketHandler.MOD_CHANNEL.sendToServer(new UpdatePortableTransmitterPacket(Integer.parseInt(frequencyField.getText()), hand));
				Minecraft.getInstance().player.closeScreen();
			}
		}));

		this.setFocusedDefault(frequencyField);

	}

	@Override
	// mappings: tick
	public void func_231023_e_() {
		super.func_231023_e_();
		this.frequencyField.tick();
	}

	@Override
	// mappings: render
	public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		// mappings: renderBackground
		this.func_230446_a_(stack);
		super.func_230430_a_(stack, mouseX, mouseY, partialTicks);

		// mappings: render
		this.frequencyField.func_230430_a_(stack, mouseX, mouseY, partialTicks);
		// mappings: renderHoveredTooltip
		this.func_230459_a_(stack, mouseX, mouseY);
	}

	@Override
	// mappings: drawGuiContainerBackgroundLayer
	protected void func_230450_a_(@Nonnull MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	// mappings: drawGuiContainerForegroundLayer
	protected void func_230451_b_(@Nonnull MatrixStack stack, int mouseX, int mouseY) {
		super.func_230451_b_(stack, mouseX, mouseY);
		// mappings: this.font.drawString
		this.field_230712_o_.func_238422_b_(stack, new TranslationTextComponent("gui.gates.frequency"), 40, 20, 14737632);
	}
}
