package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.PacketHandler;
import com.github.se7_kn8.gates.container.AdvancedRedstoneClockMenu;
import com.github.se7_kn8.gates.packages.UpdateRedstoneClockPacket;
import com.github.se7_kn8.gates.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class AdvancedRedstoneClockScreen extends BasicPlayerScreen<AdvancedRedstoneClockMenu> {

	private Button applyButton;
	private EditBox clockBox;
	private EditBox clockLengthBox;

	private int lastClockTime;
	private int lastClockLength;


	public AdvancedRedstoneClockScreen(AdvancedRedstoneClockMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}


	@Override
	protected void init() {
		super.init();

		clockBox = new EditBox(this.font, this.width / 2 - 75, this.height / 2 - 50, 70, 20, new TranslatableComponent("gui.gates.clock_time"));
		clockLengthBox = new EditBox(this.font, this.width / 2 + 5, this.height / 2 - 50, 70, 20, new TranslatableComponent("gui.gates.clock_length"));

		clockBox.setFilter(Utils.NUMBER_STRING_9_CHARACTERS);
		clockLengthBox.setFilter(Utils.NUMBER_STRING_9_CHARACTERS);

		applyButton = this.addRenderableWidget(new Button(this.width / 2, this.height / 2 - 25, 80, 20, new TranslatableComponent("gui.gates.apply"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateRedstoneClockPacket(lastBlockPos, Integer.parseInt(this.clockBox.getValue()), Integer.parseInt(this.clockLengthBox.getValue())));
		}));

		this.addWidget(clockBox);
		this.addWidget(clockLengthBox);

		this.applyButton.visible = true;
	}

	@Override
	public void containerTick() {
		this.clockLengthBox.tick();
		this.clockBox.tick();

		int clockTime = this.getMenu().getClockTime();
		int clockLength = this.getMenu().getClockLength();

		boolean change = false;
		boolean enableButton = true;

		if (clockTime != this.lastClockTime) {
			this.lastClockTime = clockTime;
			this.clockBox.setValue(String.valueOf(clockTime));
		} else {
			try {
				change = clockTime != Integer.parseInt(this.clockBox.getValue());
			} catch (NumberFormatException e) {
				// field is empty
				enableButton = false;
			}
		}

		if (clockLength != this.lastClockLength) {
			this.lastClockLength = clockLength;
			this.clockLengthBox.setValue(String.valueOf(clockLength));
		} else {
			try {
				change = change | (clockLength != Integer.parseInt(this.clockLengthBox.getValue()));
			} catch (NumberFormatException e) {
				// field is empty
				enableButton = false;
			}
		}

		this.applyButton.visible = change && enableButton;
	}

	@Override
	public void render(@Nonnull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		super.render(stack, mouseX, mouseY, partialTicks);

		this.clockBox.render(stack, mouseX, mouseY, partialTicks);
		this.clockLengthBox.render(stack, mouseX, mouseY, partialTicks);
	}


	@Override
	protected void renderLabels(@Nonnull PoseStack stack, int mouseX, int mouseY) {
		super.renderLabels(stack, mouseX, mouseY);

		this.font.draw(stack, new TranslatableComponent("gui.gates.clock_time"), 13, 20, 4210752);
		this.font.draw(stack, new TranslatableComponent("gui.gates.clock_length"), 94, 20, 4210752);
	}

}
