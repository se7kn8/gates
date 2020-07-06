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
	// mappings: init
	protected void func_231160_c_() {
		super.func_231160_c_();
		// mappings: font, width, height
		frequencyField = new TextFieldWidget(this.field_230712_o_, this.field_230708_k_ / 2 - 35, this.field_230709_l_ / 2 - 50, 70, 20, new TranslationTextComponent("gui.gates.frequency"));
		frequencyField.setValidator(Utils.NUMBER_STRING_9_CHARACTERS);

		// mappings: addButton, width, height
		this.func_230480_a_(new Button(this.field_230708_k_ / 2 - 75, this.field_230709_l_ / 2 - 50, 40, 20, new StringTextComponent("<-"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(getTilePos(), getContainer().getFrequency() - 1));
		}));

		// mappings: addButton, width, height
		this.func_230480_a_(new Button(this.field_230708_k_ / 2 + 35, this.field_230709_l_ / 2 - 50, 40, 20, new StringTextComponent("->"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(getTilePos(), getContainer().getFrequency() + 1));
		}));

		// mappings: addButton, width, height
		applyButton = this.func_230480_a_(new Button(this.field_230708_k_ / 2, this.field_230709_l_ / 2 - 25, 80, 20, new TranslationTextComponent("gui.gates.apply"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(getTilePos(), Integer.parseInt(this.frequencyField.getText())));
		}));

		// mappings: getChildren
		this.field_230705_e_.add(frequencyField);

		this.setFocusedDefault(frequencyField);

		// mapping: visible
		this.applyButton.field_230694_p_ = false;
	}


	@Override
	// mappings: tick
	public void func_231023_e_() {
		super.func_231023_e_();
		this.frequencyField.tick();
		int freq = this.getContainer().getFrequency();
		if (freq != lastValue) {
			lastValue = freq;
			this.frequencyField.setText(String.valueOf(freq));
		} else {
			try {
				this.applyButton.field_230694_p_ = freq != Integer.parseInt(this.frequencyField.getText());
			} catch (NumberFormatException e) {
				// field is empty
				this.applyButton.field_230694_p_ = false;
			}
		}
	}

	@Override
	// mappings: render
	protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		super.func_230450_a_(stack, partialTicks, mouseX, mouseY);

		// mappings: render
		this.frequencyField.func_230430_a_(stack, mouseX, mouseY, partialTicks);
	}


	@Override
	// mappings: drawGuiContainerForegroundLayer
	protected void func_230451_b_(@Nonnull MatrixStack stack, int mouseX, int mouseY) {
		super.func_230451_b_(stack, mouseX, mouseY);
		// mappings: this.font.drawString
		this.field_230712_o_.func_238422_b_(stack, new TranslationTextComponent("gui.gates.frequency"), 40, 20, 4210752);
	}


}
