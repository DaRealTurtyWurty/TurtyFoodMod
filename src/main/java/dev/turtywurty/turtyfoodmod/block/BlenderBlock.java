package dev.turtywurty.turtyfoodmod.block;

import com.mojang.serialization.MapCodec;
import dev.turtywurty.turtyfoodmod.block.entity.BlenderBlockEntity;
import dev.turtywurty.turtyfoodmod.block.entity.util.SyncedEnergyStorage;
import dev.turtywurty.turtyfoodmod.block.entity.util.TickableBlockEntity;
import dev.turtywurty.turtyfoodmod.init.BlockEntityTypeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlenderBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final MapCodec<BlenderBlock> CODEC = simpleCodec(BlenderBlock::new);

    public static final BooleanProperty RUNNING = BooleanProperty.create("running");

    public BlenderBlock(Properties pProperties) {
        super(pProperties);

        registerDefaultState(this.stateDefinition.any()
                .setValue(RUNNING, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(RUNNING, FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityTypeInit.BLENDER.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return TickableBlockEntity.getTicker(pLevel);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            if (pLevel.getBlockEntity(pPos) instanceof BlenderBlockEntity blenderBlockEntity) {
                ((ServerPlayer) pPlayer).openMenu(blenderBlockEntity, pPos);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            if (pLevel.getBlockEntity(pPos) instanceof BlenderBlockEntity blenderBlockEntity) {
                blenderBlockEntity.dropContents();

                SyncedEnergyStorage energyStorage = blenderBlockEntity.getEnergy();
                if (energyStorage != null) {
                    // TODO: energy particles
                }
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);

        if (pState.getValue(RUNNING)) {
            // TODO: play blender sound
        }
    }
}
