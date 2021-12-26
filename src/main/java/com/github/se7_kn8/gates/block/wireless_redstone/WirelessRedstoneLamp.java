package com.github.se7_kn8.gates.block.wireless_redstone;

import com.github.se7_kn8.gates.tile.ReceiverTileEntity;
import com.github.se7_kn8.gates.util.WirelessRedstoneUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class WirelessRedstoneLamp extends BaseEntityBlock implements ReceiverTileEntity.IWirelessReceiver {

	public static BooleanProperty LIT = BlockStateProperties.LIT;

	public WirelessRedstoneLamp() {
		super(Properties.copy(Blocks.REDSTONE_LAMP));
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(LIT);
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		return WirelessRedstoneUtil.use(pLevel, pPos, pPlayer, pHand);
	}

	@Override
	public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
		WirelessRedstoneUtil.onPlace(pState, pLevel, pPos, pOldState);
		super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		WirelessRedstoneUtil.onRemove(pState, pLevel, pPos, pNewState);
		super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public void onPowerChange(Level level, BlockPos pos, int newPower) {
		level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(LIT, newPower > 0));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		ReceiverTileEntity entity = new ReceiverTileEntity(pPos, pState);
		entity.setTranslationKey("gates.block.wireless_redstone_lamp");
		return entity;
	}
}
