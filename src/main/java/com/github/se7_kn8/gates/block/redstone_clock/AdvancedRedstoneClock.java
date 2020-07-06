package com.github.se7_kn8.gates.block.redstone_clock;

import com.github.se7_kn8.gates.block.redstone_clock.RedstoneClockBaseBlock;
import com.github.se7_kn8.gates.tile.RedstoneClockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;


public class AdvancedRedstoneClock extends RedstoneClockBaseBlock {

	public AdvancedRedstoneClock() {
		setDefaultState(this.stateContainer.getBaseState().with(POWERED, false));
	}
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (!world.isRemote) {
			TileEntity entity = world.getTileEntity(pos);
			if (entity instanceof RedstoneClockTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) entity, entity.getPos());
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(POWERED);
	}
}
