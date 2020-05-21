package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.PacketHandler;
import com.github.se7_kn8.gates.container.AdvancedRedstoneClockContainer;
import com.github.se7_kn8.gates.packages.UpdateRedstoneClockPacket;
import com.github.se7_kn8.gates.util.Utils;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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

		clockTimeField = new TextFieldWidget(this.font, this.width / 2 - 75, this.height / 2 - 50, 70, 20, I18n.format("gui.gates.clock_time"));
		clockLengthField = new TextFieldWidget(this.font, this.width / 2 + 5, this.height / 2 - 50, 70, 20, I18n.format("gui.gates.clock_length"));

		clockTimeField.setValidator(Utils.NUMBER_STRING_9_CHARACTERS);
		clockLengthField.setValidator(Utils.NUMBER_STRING_9_CHARACTERS);
		applyButton = this.addButton(new Button(this.width / 2, this.height / 2 - 25, 80, 20, I18n.format("gui.gates.apply"), p_onPress_1_ -> {
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
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);

		this.clockTimeField.render(mouseX, mouseY, partialTicks);
		this.clockLengthField.render(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.font.drawString(new TranslationTextComponent("gui.gates.clock_time").getFormattedText(), 13, 20, 4210752);
		this.font.drawString(new TranslationTextComponent("gui.gates.clock_length").getFormattedText(), 94, 20, 4210752);
	}

}
