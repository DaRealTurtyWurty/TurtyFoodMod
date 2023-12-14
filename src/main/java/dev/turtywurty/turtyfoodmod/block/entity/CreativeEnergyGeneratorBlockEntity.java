package dev.turtywurty.turtyfoodmod.block.entity;

import dev.turtywurty.turtyfoodmod.block.entity.util.ModifiableEnergyStorage;
import dev.turtywurty.turtyfoodmod.block.entity.util.TickableBlockEntity;
import dev.turtywurty.turtyfoodmod.init.BlockEntityTypeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class CreativeEnergyGeneratorBlockEntity extends BlockEntity implements TickableBlockEntity {
    public CreativeEnergyGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityTypeInit.CREATIVE_ENERGY_GENERATOR.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;

        for (Direction direction : Direction.values()) {
            BlockPos pos = worldPosition.relative(direction);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null)
                continue;

            blockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(energyStorage -> {
                if(energyStorage instanceof ModifiableEnergyStorage modifiableEnergyStorage) {
                    modifiableEnergyStorage.giveEnergy(100);
                } else {
                    energyStorage.receiveEnergy(100, false);
                }
            });
        }
    }
}
