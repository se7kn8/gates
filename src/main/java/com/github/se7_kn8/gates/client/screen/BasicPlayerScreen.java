package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.Gates;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;

public abstract class BasicPlayerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

	protected ResourceLocation background;
	protected BlockPos lastBlockPos;

	public BasicPlayerScreen(T screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		background = new ResourceLocation(Gates.MODID, "textures/gui/container/empty_container.png");
		if (Minecraft.getInstance().hitResult instanceof BlockHitResult b) {
			lastBlockPos = b.getBlockPos();
		}
	}

	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		this.renderBackground(pPoseStack);
		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		this.renderTooltip(pPoseStack, pMouseX, pMouseY);
	}

	@Override
	protected void renderBg(@Nonnull PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, background);
		this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}
}
