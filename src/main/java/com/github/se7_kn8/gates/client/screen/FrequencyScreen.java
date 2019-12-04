package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.PacketHandler;
import com.github.se7_kn8.gates.container.FrequencyContainer;
import com.github.se7_kn8.gates.packages.UpdateFrequencyPacket;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class FrequencyScreen extends ContainerScreen<FrequencyContainer> {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(Gates.MODID, "textures/gui/container/empty_container.png");

	private TextFieldWidget frequencyField;
	private Button decreaseButton;
	private Button increaseButton;

	private int lastValue;

	public FrequencyScreen(FrequencyContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();
		frequencyField = new TextFieldWidget(this.font, this.width / 2 - 35, this.height / 2 - 50, 70, 20, I18n.format("gates.gui.transmitter"));
		frequencyField.setValidator(s -> (s.matches("^[0-9]+$") || s.equals("")) && s.length() < 10);
		decreaseButton = this.addButton(new Button(this.width / 2 - 75, this.height / 2 - 50, 40, 20, "<-", p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(getContainer().entity.getPos(), getContainer().getFrequency() - 1, minecraft.player.dimension));
		}));

		increaseButton = this.addButton(new Button(this.width / 2 + 35, this.height / 2 - 50, 40, 20, "->", p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(getContainer().entity.getPos(), getContainer().getFrequency() + 1, minecraft.player.dimension));
		}));

		this.children.add(frequencyField);

		this.setFocusedDefault(frequencyField);
	}

	@Override
	public void tick() {
		this.frequencyField.tick();
		int freq = this.getContainer().getFrequency();
		if (freq != lastValue) {
			lastValue = freq;
			this.frequencyField.setText(String.valueOf(freq));
		}
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
		this.minecraft.getTextureManager().bindTexture(BACKGROUND);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(this.title.getFormattedText(), 8.0F, 4.0F, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 94), 4210752);
		this.font.drawString(new TranslationTextComponent("gui.gates.frequency").getFormattedText(), 40, 20, 4210752);
	}

}
