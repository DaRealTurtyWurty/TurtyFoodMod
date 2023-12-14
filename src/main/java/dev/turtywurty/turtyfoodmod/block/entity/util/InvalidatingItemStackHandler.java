package dev.turtywurty.turtyfoodmod.block.entity.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class InvalidatingItemStackHandler extends ItemStackHandler {
    private final LazyOptional<ItemStackHandler> lazyOptional = LazyOptional.of(() -> this);

    public InvalidatingItemStackHandler() {
        super();
    }

    public InvalidatingItemStackHandler(int size) {
        super(size);
    }

    public InvalidatingItemStackHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    public LazyOptional<ItemStackHandler> getLazyOptional() {
        return this.lazyOptional;
    }

    public void invalidate() {
        this.lazyOptional.invalidate();
    }
}
