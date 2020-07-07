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
	protected void init() {
		super.init();

		clockTimeField = new TextFieldWidget(this.font, this.width / 2 - 75, this.height / 2 - 50, 70, 20, new TranslationTextComponent("gui.gates.clock_time"));
		clockLengthField = new TextFieldWidget(this.font, this.width / 2 + 5, this.height / 2 - 50, 70, 20, new TranslationTextComponent("gui.gates.clock_length"));

		clockTimeField.setValidator(Utils.NUMBER_STRING_9_CHARACTERS);
		clockLengthField.setValidator(Utils.NUMBER_STRING_9_CHARACTERS);

		applyButton = this.addButton(new Button(this.width / 2, this.height / 2 - 25, 80, 20, new TranslationTextComponent("gui.gates.apply"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateRedstoneClockPacket(getTilePos(), Integer.parseInt(this.clockTimeField.getText()), Integer.parseInt(this.clockLengthField.getText())));
		}));

		this.children.add(clockTimeField);
		this.children.add(clockLengthField);

		this.applyButton.visible = false;
	}

	@Override
	public void tick() {
		super.tick();
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

		this.applyButton.visible = change && enableButton;
	}

	@Override
	public void render(@Nonnull MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		super.render(stack, mouseX, mouseY, partialTicks);

		this.clockTimeField.render(stack, mouseX, mouseY, partialTicks);
		this.clockLengthField.render(stack, mouseX, mouseY, partialTicks);
	}

	@Override
	// mappings: drawGuiContainerForegroundLayer
	protected void func_230451_b_(@Nonnull MatrixStack stack, int mouseX, int mouseY) {
		super.func_230451_b_(stack, mouseX, mouseY);

		// mappings: drawString
		this.font.func_238422_b_(stack, new TranslationTextComponent("gui.gates.clock_time"), 13, 20, 4210752);
		this.font.func_238422_b_(stack, new TranslationTextComponent("gui.gates.clock_length"), 94, 20, 4210752);
	}

}
