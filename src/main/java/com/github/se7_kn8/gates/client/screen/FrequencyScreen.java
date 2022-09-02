package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.PacketHandler;
import com.github.se7_kn8.gates.menu.FrequencyMenu;
import com.github.se7_kn8.gates.packages.UpdateFrequencyPacket;
import com.github.se7_kn8.gates.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class FrequencyScreen extends BasicPlayerScreen<FrequencyMenu> {

	private EditBox frequencyBox;
	private Button applyButton;

	private int lastValue;

	public FrequencyScreen(FrequencyMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();

		frequencyBox = new EditBox(this.font, this.width / 2 - 35, this.height / 2 - 50, 70, 20, Component.translatable("gui.gates.frequency"));
		frequencyBox.setFilter(Utils.NUMBER_STRING_9_CHARACTERS);

		this.addWidget(new Button(this.width / 2 - 75, this.height / 2 - 50, 40, 20, Component.literal("<-"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(lastBlockPos, getMenu().getFrequency() - 1));
		}));

		this.addWidget(new Button(this.width / 2 + 35, this.height / 2 - 50, 40, 20, Component.literal("->"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(lastBlockPos, getMenu().getFrequency() + 1));
		}));

		applyButton = this.addRenderableWidget(new Button(this.width / 2, this.height / 2 - 25, 80, 20, Component.translatable("gui.gates.apply"), p_onPress_1_ -> {
			PacketHandler.MOD_CHANNEL.sendToServer(new UpdateFrequencyPacket(lastBlockPos, Integer.parseInt(this.frequencyBox.getValue())));
		}));

		this.addWidget(frequencyBox);

		this.setInitialFocus(frequencyBox);

		this.applyButton.visible = false;
	}


	@Override
	public void containerTick() {
		this.frequencyBox.tick();
		int freq = this.getMenu().getFrequency();
		if (freq != lastValue) {
			lastValue = freq;
			this.frequencyBox.setValue(String.valueOf(freq));
		} else {
			try {
				this.applyButton.visible = freq != Integer.parseInt(this.frequencyBox.getValue());
			} catch (NumberFormatException e) {
				// field is empty
				this.applyButton.visible = false;
			}
		}
	}

	@Override
	public void render(@Nonnull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		super.render(stack, mouseX, mouseY, partialTicks);

		this.frequencyBox.render(stack, mouseX, mouseY, partialTicks);
	}

	private static final Component COMPONENT_FREQUENCY = Component.translatable("gui.gates.frequency");

	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
		super.renderLabels(pPoseStack, pMouseX, pMouseY);
		this.font.draw(pPoseStack, COMPONENT_FREQUENCY, 40, 20, 4210752);
	}

}
