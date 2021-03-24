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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
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
		frequencyField.setFilter(s -> (s.matches("^[0-9]+$") || s.equals("")) && s.length() < 10);

		this.children.add(frequencyField);

		this.addButton(new Button(this.width / 2 - 40, this.height / 2, 80, 20, new TranslationTextComponent("gui.gates.apply"), button -> {
			if (!frequencyField.getValue().trim().equals("")) {
				Item item = Minecraft.getInstance().player.getItemInHand(Hand.MAIN_HAND).getItem();
				Hand hand = item == GatesItems.PORTABLE_REDSTONE_TRANSMITTER.get() ? Hand.MAIN_HAND : Hand.OFF_HAND;
				PacketHandler.MOD_CHANNEL.sendToServer(new UpdatePortableTransmitterPacket(Integer.parseInt(frequencyField.getValue()), hand));
				Minecraft.getInstance().player.closeContainer();
			}
		}));

		this.setInitialFocus(frequencyField);

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
		this.renderTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(@Nonnull MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	protected void renderLabels(@Nonnull MatrixStack stack, int mouseX, int mouseY) {
		super.renderLabels(stack, mouseX, mouseY);
		// mappings: drawString
		this.font.draw(stack, new TranslationTextComponent("gui.gates.frequency"), 40, 20, 14737632);
	}
}
