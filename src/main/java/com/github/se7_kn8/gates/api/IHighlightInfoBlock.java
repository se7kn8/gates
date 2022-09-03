package com.github.se7_kn8.gates.api;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IHighlightInfoBlock {

	Direction getHighlightFacing(BlockState state);
	default String getHighlightInfo(BlockState state, Direction direction){
		return switch(direction) {
			case NORTH -> "##north##";
			case SOUTH -> "##south##";
			case WEST -> "##west##";
			case EAST -> "##east##";
			default -> "";
		};
	}

}
