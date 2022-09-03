package com.github.se7_kn8.gates.mixin;

import com.github.se7_kn8.gates.api.IHighlightInfoBlock;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RepeaterBlock.class)
public abstract class RepeaterBlockMixin extends DiodeBlock implements IHighlightInfoBlock {

	protected RepeaterBlockMixin(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public Direction getHighlightFacing(BlockState state) {
		return state.getValue(HorizontalDirectionalBlock.FACING);
	}

	@Override
	public String getHighlightInfo(BlockState state, Direction direction) {
		return switch(direction) {
			case NORTH -> I18n.get("info.gates.output");
			case SOUTH -> I18n.get("info.gates.input");
			case WEST, EAST -> I18n.get("info.gates.lock");
			default -> "";
		};
	}
}
