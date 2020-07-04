package com.github.se7_kn8.gates.client.screen;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.container.BasicPlayerContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public abstract class BasicPlayerScreen<T extends BasicPlayerContainer> extends ContainerScreen<T> {

	protected ResourceLocation background;

	public BasicPlayerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		background = new ResourceLocation(Gates.MODID, "textures/gui/container/empty_container.png");
	}

	@Override
	public void render(int mouseX, int mouseY, float ticks) {
		this.renderBackground();
		super.render(mouseX, mouseY, ticks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(background);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(this.title.getFormattedText(), 8.0F, 4.0F, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 94), 4210752);
	}

	public TileEntity getTile() {
		return getContainer().getTile();
	}

	public BlockPos getTilePos(){
		return getTile().getPos();
	}

}