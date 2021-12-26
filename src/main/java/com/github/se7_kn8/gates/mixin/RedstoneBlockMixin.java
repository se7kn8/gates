package com.github.se7_kn8.gates.mixin;

import com.github.se7_kn8.gates.api.IRedstoneWire;
import com.github.se7_kn8.gates.util.RedstoneHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public abstract class RedstoneBlockMixin implements IRedstoneWire {

	@Inject(at = @At("HEAD"), method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", cancellable = true, remap = false)
	private static void canConnectTo(BlockState blockState, Direction side, CallbackInfoReturnable<Boolean> info) {
		if (blockState.getBlock() instanceof IRedstoneWire) {
			info.setReturnValue(true);
		}
	}

	@Inject(at = @At("HEAD"), method = "getWireSignal", cancellable = true)
	private void getPower(BlockState state, CallbackInfoReturnable<Integer> info) {
		if (state.getBlock() instanceof IRedstoneWire) {
			info.setReturnValue(state.getValue(RedStoneWireBlock.POWER));
		}
	}

	@Inject(at = @At("HEAD"), method = "checkCornerChangeAt", cancellable = true)
	private void notifyWireNeighborsOfStateChange(Level level, BlockPos pos, CallbackInfo info) {
		if (level.getBlockState(pos).getBlock() instanceof IRedstoneWire) {
			level.updateNeighborsAt(pos, ((RedStoneWireBlock) (Object) this));

			for (Direction direction : Direction.values()) {
				level.updateNeighborsAt(pos.relative(direction), ((RedStoneWireBlock) (Object) this));
			}
			info.cancel();
		}
	}

	@Redirect(method = "calculateTargetStrength", at = @At(value = "FIELD", target = "net/minecraft/world/level/block/RedStoneWireBlock.shouldSignal:Z"), require = 2)
	private void redirectPowerWrite(RedStoneWireBlock owner, boolean value) {
		RedstoneHelper.shouldSignal = value;
	}

	@Redirect(method = {"getSignal", "getDirectSignal", "isSignalSource"}, at = @At(value = "FIELD", target = "net/minecraft/world/level/block/RedStoneWireBlock.shouldSignal:Z"), require = 3)
	private boolean redirectPowerRead(RedStoneWireBlock owner) {
		return RedstoneHelper.shouldSignal;
	}

	@Redirect(method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canRedstoneConnectTo(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"), require = 3)
	private boolean redirectShouldConnectTo(BlockState instance, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
		if (instance.getBlock() instanceof IRedstoneWire) {
			return true;
		}
		return instance.canRedstoneConnectTo(blockGetter, blockPos, direction);
	}

}
