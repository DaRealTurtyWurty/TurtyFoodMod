package dev.turtywurty.turtyfoodmod.block.entity.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class SyncedItemStackHandler extends InvalidatingItemStackHandler {
    private final Consumer<ItemStackHandler> syncFunction;

    @ApiStatus.Internal
    private SyncedItemStackHandler(NonNullList<ItemStack> stacks, Consumer<ItemStackHandler> syncFunction) {
        super(stacks);
        this.syncFunction = syncFunction;
    }

    public static Builder create(@NotNull Consumer<ItemStackHandler> syncFunction) {
        return new Builder(syncFunction);
    }

    public static Builder create(@NotNull SyncedBlockEntity blockEntity) {
        return new Builder(blockEntity);
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        this.syncFunction.accept(this);
    }

    public static class Builder {
        private final Consumer<ItemStackHandler> syncFunction;
        private NonNullList<ItemStack> stacks;

        public Builder(@NotNull Consumer<ItemStackHandler> syncFunction) {
            this.syncFunction = syncFunction;
        }

        public Builder(@NotNull SyncedBlockEntity blockEntity) {
            this.syncFunction = ignored -> blockEntity.sync();
        }

        public Builder stacks(NonNullList<ItemStack> stacks) {
            this.stacks = stacks;
            return this;
        }

        public Builder stacks(int size) {
            this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
            return this;
        }

        public Builder stacks(List<ItemStack> stacks) {
            this.stacks = NonNullList.of(ItemStack.EMPTY, stacks.toArray(new ItemStack[0]));
            return this;
        }

        public SyncedItemStackHandler build() {
            return new SyncedItemStackHandler(
                    this.stacks == null ? NonNullList.withSize(1, ItemStack.EMPTY) : this.stacks,
                    this.syncFunction);
        }
    }
}
