package com.github.se7_kn8.gates.mixin;

import com.github.se7_kn8.gates.api.IRedstoneWire;
import com.github.se7_kn8.gates.util.RedstoneHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneBlockMixin implements IRedstoneWire{

	@Inject(at = @At("HEAD"), method = "canConnectTo", cancellable = true, remap = false)
	private static void canConnectTo(BlockState blockState, IBlockReader world, BlockPos pos, @Nullable Direction side, CallbackInfoReturnable<Boolean> info) {
		if (blockState.getBlock() instanceof IRedstoneWire) {
			info.setReturnValue(true);
		}
	}

	@Inject(at = @At("HEAD"), method = "getWireSignal", cancellable = true)
	private void getWireSignal(BlockState state, CallbackInfoReturnable<Integer> info) {
		if (state.getBlock() instanceof IRedstoneWire) {
			info.setReturnValue(state.getValue(RedstoneWireBlock.POWER));
		}
	}

	@Inject(at = @At("HEAD"), method = "updateNeighborsOfNeighboringWires", cancellable = true)
	private void notifyWireNeighborsOfStateChange(World world, BlockPos pos, CallbackInfo info) {
		if (world.getBlockState(pos).getBlock() instanceof IRedstoneWire) {
			world.updateNeighborsAt(pos, ((RedstoneWireBlock) (Object) this));

			for (Direction direction : Direction.values()) {
				world.updateNeighborsAt(pos.relative(direction), ((RedstoneWireBlock) (Object) this));
			}
			info.cancel();
		}
	}

	@Redirect(method = "calculateTargetStrength", at = @At(value = "FIELD", target = "net/minecraft/block/RedstoneWireBlock.shouldSignal:Z"), require = 2)
	private void redirectPowerWrite(RedstoneWireBlock owner, boolean value) {
		RedstoneHelper.shouldSignal = value;
	}

	@Redirect(method = {"getDirectSignal", "getSignal", "isSignalSource"}, at = @At(value = "FIELD", target = "net/minecraft/block/RedstoneWireBlock.shouldSignal:Z"), require = 3)
	private boolean redirectPowerRead(RedstoneWireBlock owner) {
		return RedstoneHelper.shouldSignal;
	}
}
