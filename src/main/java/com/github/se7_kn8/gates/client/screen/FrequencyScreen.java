package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.PacketHandler;
import com.github.se7_kn8.gates.container.FrequencyContainer;
import com.github.se7_kn8.gates.packages.UpdateFrequencyPacket;
import com.github.se7_kn8.gates.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class FrequencyScreen extends BasicPlayerScreen<FrequencyContainer> {

	private TextFieldWidget frequencyField;
	private Button applyButton;

	private int lastValue;

	public FrequencyScreen(FrequencyContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();
		frequencyField = new TextFieldWidget(this.font, this.width / 2 - 35, this.height / 2 - 50, 70, 20, new TranslationTextComponent("gui.gates.frequency"));
		frequencyField.setFilter(Utils.NUMBER_STRING_9_CHARACTERS);

		this.addButton(new Button(this.width / 2 - 75, this.height / 2 - 50, 40, 20, new StringTextComponent("<-"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(getTilePos(), getMenu().getFrequency() - 1));
		}));

		this.addButton(new Button(this.width / 2 + 35, this.height / 2 - 50, 40, 20, new StringTextComponent("->"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(getTilePos(), getMenu().getFrequency() + 1));
		}));

		applyButton = this.addButton(new Button(this.width / 2, this.height / 2 - 25, 80, 20, new TranslationTextComponent("gui.gates.apply"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(getTilePos(), Integer.parseInt(this.frequencyField.getValue())));
		}));

		this.children.add(frequencyField);

		this.setInitialFocus(frequencyField);

		this.applyButton.visible = false;
	}


	@Override
	public void tick() {
		super.tick();
		this.frequencyField.tick();
		int freq = this.getMenu().getFrequency();
		if (freq != lastValue) {
			lastValue = freq;
			this.frequencyField.setValue(String.valueOf(freq));
		} else {
			try {
				this.applyButton.visible = freq != Integer.parseInt(this.frequencyField.getValue());
			} catch (NumberFormatException e) {
				// field is empty
				this.applyButton.visible = false;
			}
		}
	}

	@Override
	public void render(@Nonnull MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		super.render(stack, mouseX, mouseY, partialTicks);

		this.frequencyField.render(stack, mouseX, mouseY, partialTicks);
	}


	@Override
	protected void renderLabels(@Nonnull MatrixStack stack, int mouseX, int mouseY) {
		super.renderLabels(stack, mouseX, mouseY);
		// mappings: drawString
		this.font.draw(stack, new TranslationTextComponent("gui.gates.frequency"), 40, 20, 4210752);
	}


}
