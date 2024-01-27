package dev.turtywurty.turtyfoodmod.block.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class SyncedBlockEntity extends BlockEntity {
    public SyncedBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public CompoundTag getReadSyncData() {
        return saveWithoutMetadata();
    }

    public void applyWriteSyncData(CompoundTag pCompound) {
        load(pCompound);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return getReadSyncData();
    }

    @Override
    public void handleUpdateTag(CompoundTag pTag) {
        applyWriteSyncData(pTag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> getReadSyncData());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        applyWriteSyncData(pkt.getTag());
    }

    public void sync() {
        if (level != null && !level.isClientSide) {
            setChanged();
            BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
        }
    }
}
