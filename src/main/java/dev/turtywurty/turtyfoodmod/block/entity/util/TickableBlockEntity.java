package dev.turtywurty.turtyfoodmod.block.entity.util;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public interface TickableBlockEntity {
    void tick();

    static <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel) {
        return getTicker(pLevel, false);
    }

    static <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, boolean client) {
        return pLevel.isClientSide() && !client ? null :
                ((level, pos, state, blockEntity) -> ((TickableBlockEntity) blockEntity).tick());

    }
}
