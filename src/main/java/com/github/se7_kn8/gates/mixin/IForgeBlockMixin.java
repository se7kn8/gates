// Maybe needed if some mods use Block-canConnectRedstone(state, level, pos, null), because when the direction is null, the method from the block will not be called
//package com.github.se7_kn8.gates.mixin;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.common.extensions.IForgeBlock;
//import org.jetbrains.annotations.Nullable;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//
//@Mixin(IForgeBlock.class)
//public interface IForgeBlockMixin {
//
//	/**
//	 * @author se7kn8
//	 * @reason The default check only looks for RedstoneWireBlocks but we need it to look for IRedstoneWireBlocks
//	 */
//	@Overwrite(remap = false)
//	default boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
//		return false;
//	}
//
//}
