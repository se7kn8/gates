package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.PacketHandler;
import com.github.se7_kn8.gates.container.AdvancedRedstoneClockContainer;
import com.github.se7_kn8.gates.packages.UpdateRedstoneClockPacket;
import com.github.se7_kn8.gates.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class AdvancedRedstoneClockScreen extends BasicPlayerScreen<AdvancedRedstoneClockContainer> {

	private Button applyButton;
	private TextFieldWidget clockTimeField;
	private TextFieldWidget clockLengthField;

	private int lastClockTime;
	private int lastClockLength;

	public AdvancedRedstoneClockScreen(AdvancedRedstoneClockContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	// mappings: init
	protected void func_231160_c_() {
		super.func_231160_c_();

		// mappings: font, width, height
		clockTimeField = new TextFieldWidget(this.field_230712_o_, this.field_230708_k_ / 2 - 75, this.field_230709_l_ / 2 - 50, 70, 20, new TranslationTextComponent("gui.gates.clock_time"));
		clockLengthField = new TextFieldWidget(this.field_230712_o_, this.field_230708_k_ / 2 + 5, this.field_230709_l_ / 2 - 50, 70, 20, new TranslationTextComponent("gui.gates.clock_length"));

		clockTimeField.setValidator(Utils.NUMBER_STRING_9_CHARACTERS);
		clockLengthField.setValidator(Utils.NUMBER_STRING_9_CHARACTERS);
		// mappings: addButton, width, height
		applyButton = this.func_230480_a_(new Button(this.field_230708_k_ / 2, this.field_230709_l_ / 2 - 25, 80, 20, new TranslationTextComponent("gui.gates.apply"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateRedstoneClockPacket(getTilePos(), Integer.parseInt(this.clockTimeField.getText()), Integer.parseInt(this.clockLengthField.getText())));
		}));

		// mappings: children
		this.field_230705_e_.add(clockTimeField);
		this.field_230705_e_.add(clockLengthField);

		// mappings: visible
		this.applyButton.field_230694_p_ = false;
	}

	@Override
	// mappings: tick
	public void func_231023_e_() {
		super.func_231023_e_();
		this.clockLengthField.tick();
		this.clockTimeField.tick();

		int clockTime = this.getContainer().getClockTime();
		int clockLength = this.getContainer().getClockLength();

		boolean change = false;
		boolean enableButton = true;

		if (clockTime != this.lastClockTime) {
			this.lastClockTime = clockTime;
			this.clockTimeField.setText(String.valueOf(clockTime));
		} else {
			try {
				change = clockTime != Integer.parseInt(this.clockTimeField.getText());
			} catch (NumberFormatException e) {
				// field is empty
				enableButton = false;
			}
		}

		if (clockLength != this.lastClockLength) {
			this.lastClockLength = clockLength;
			this.clockLengthField.setText(String.valueOf(clockLength));
		} else {
			try {
				change = change | (clockLength != Integer.parseInt(this.clockLengthField.getText()));
			} catch (NumberFormatException e) {
				// field is empty
				enableButton = false;
			}
		}

		// mappings: visible
		this.applyButton.field_230694_p_ = change && enableButton;
	}

	@Override
	// mappings: render
	public void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		super.func_230450_a_(stack, partialTicks, mouseX, mouseY);

		// mappings: render
		this.clockTimeField.func_230430_a_(stack, mouseX, mouseY, partialTicks);
		this.clockLengthField.func_230430_a_(stack, mouseX, mouseY, partialTicks);
	}

	@Override
	// mappings: drawGuiContainerForegroundLayer
	protected void func_230451_b_(@Nonnull MatrixStack stack, int mouseX, int mouseY) {
		super.func_230451_b_(stack, mouseX, mouseY);

		// mappings: this.font.drawString
		this.field_230712_o_.func_238422_b_(stack, new TranslationTextComponent("gui.gates.clock_time"), 13, 20, 4210752);
		this.field_230712_o_.func_238422_b_(stack, new TranslationTextComponent("gui.gates.clock_length"), 94, 20, 4210752);
	}

}
