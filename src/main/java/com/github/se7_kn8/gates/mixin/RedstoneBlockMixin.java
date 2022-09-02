package com.github.se7_kn8.gates.mixin;

import com.github.se7_kn8.gates.api.IRedstoneWire;
import com.github.se7_kn8.gates.util.RedstoneHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RedStoneWireBlock.class)
public abstract class RedstoneBlockMixin implements IRedstoneWire {

	@Redirect(method = {"checkCornerChangeAt", "updateIndirectNeighbourShapes", "getWireSignal"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"), require = 5)
	private boolean redirectIsRedstoneWire(BlockState instance, Block block) {
		if(instance.getBlock() instanceof IRedstoneWire) {
			return true;
		}
		return instance.is(block);
	}

	@Redirect(method = "calculateTargetStrength", at = @At(value = "FIELD", target = "net/minecraft/world/level/block/RedStoneWireBlock.shouldSignal:Z"), require = 2)
	private void redirectPowerWrite(RedStoneWireBlock owner, boolean value) {
		RedstoneHelper.shouldSignal = value;
	}

	@Redirect(method = {"getSignal", "getDirectSignal", "isSignalSource"}, at = @At(value = "FIELD", target = "net/minecraft/world/level/block/RedStoneWireBlock.shouldSignal:Z"), require = 3)
	private boolean redirectPowerRead(RedStoneWireBlock owner) {
		return RedstoneHelper.shouldSignal;
	}

	@Redirect(method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canRedstoneConnectTo(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z", remap = false), require = 3)
	private boolean redirectShouldConnectTo(BlockState instance, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
		if (instance.getBlock() instanceof IRedstoneWire) {
			return true;
		}
		return instance.canRedstoneConnectTo(blockGetter, blockPos, direction);
	}

}
