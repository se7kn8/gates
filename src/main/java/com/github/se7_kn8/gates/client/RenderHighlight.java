package com.github.se7_kn8.gates.client;

import com.github.se7_kn8.gates.Gates;
import com.github.se7_kn8.gates.api.IHighlightInfoBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Gates.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderHighlight {

	@SubscribeEvent
	public static void onDrawHighlight(RenderHighlightEvent.Block event) {
		// Check if there's an active level
		if (Minecraft.getInstance().level != null && Gates.config.renderInfoHighlights.get()) {
			BlockState state = Minecraft.getInstance().level.getBlockState(event.getTarget().getBlockPos());
			// Only render on instances of the interface
			if(state.getBlock() instanceof IHighlightInfoBlock block) {

				// Bring the pose stack the current block highlight and rotate it to the origin position
				PoseStack stack = event.getPoseStack();
				stack.pushPose();
				Vec3 cameraPosition = event.getCamera().getPosition();
				BlockPos pos = event.getTarget().getBlockPos();
				stack.translate(pos.getX() - cameraPosition.x + 0.5, pos.getY() - cameraPosition.y + 0.130, pos.getZ() - cameraPosition.z + 0.5);
				stack.mulPose(Vector3f.XP.rotationDegrees(90.0f));

				int blockRotation = switch(block.getHighlightFacing(state)) {
					case NORTH -> 0;
					case WEST -> 90;
					case SOUTH -> 180;
					case EAST -> 270;
					default -> 50;
				};

				// We want one text for every horizontal axis
				for (int i = 0; i < 4; i++) {

					stack.pushPose();

					// Calc the rotation and the offset for every text
					int rotation = i * 90;
					stack.mulPose(Vector3f.ZP.rotationDegrees(rotation));
					stack.translate(0.0,-0.52,0.0);
					stack.scale(-0.015f, -0.015f, 0.015f);
					Matrix4f matrix4f = stack.last().pose();

					rotation += blockRotation;

					if(rotation >= 360) {
						rotation -= 360;
					}

					String tooltip = block.getHighlightInfo(state, Direction.fromYRot(rotation));

					if(!tooltip.isBlank()) {

						Font font = Minecraft.getInstance().font;
						float xOffset = (float) (-font.width(tooltip) / 2);

						// Some code from minecraft to calc the background color
						float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
						int j = (int) (f1 * 255.0F) << 24;

						font.drawInBatch(tooltip, xOffset, 0, -1, false, matrix4f, event.getMultiBufferSource(), false, j, 15728880);
					}
					stack.popPose();
				}
				stack.popPose();

			}
		}
	}

}
