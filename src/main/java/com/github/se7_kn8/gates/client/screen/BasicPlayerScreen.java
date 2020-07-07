package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.container.BasicPlayerContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public abstract class BasicPlayerScreen<T extends BasicPlayerContainer> extends ContainerScreen<T> {

	protected ResourceLocation background;

	public BasicPlayerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		background = new ResourceLocation(Gates.MODID, "textures/gui/container/empty_container.png");
	}

	@Override
	public void render(@Nonnull MatrixStack stack, int mouseX, int mouseY, float ticks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, ticks);

		// mappings: renderHoveredMouseTooltip
		this.func_230459_a_(stack, mouseX, mouseY);
	}

	@Override
	// mappings: drawGuiContainerBackgroundLayer
	protected void func_230450_a_(@Nonnull MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(background);
		this.blit(stack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	public TileEntity getTile() {
		return getContainer().getTile();
	}

	public BlockPos getTilePos() {
		return getTile().getPos();
	}

}
