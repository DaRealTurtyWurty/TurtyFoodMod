package dev.turtywurty.turtyfoodmod.block.entity.util;

import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

public class SyncedEnergyStorage extends InvalidatingEnergyStorage {
    private final Consumer<SyncedEnergyStorage> syncFunction;

    @ApiStatus.Internal
    private SyncedEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, Consumer<SyncedEnergyStorage> syncFunction) {
        super(capacity, maxReceive, maxExtract, energy);
        this.syncFunction = syncFunction;
    }

    public static Builder create(Consumer<SyncedEnergyStorage> syncFunction) {
        return new Builder(syncFunction);
    }

    public static Builder create(SyncedBlockEntity blockEntity) {
        return new Builder(blockEntity);
    }

    @Override
    public void onEnergyChanged() {
        super.onEnergyChanged();
        this.syncFunction.accept(this);
    }

    public static class Builder {
        private final Consumer<SyncedEnergyStorage> syncFunction;
        private int capacity = 1000, maxReceive = 100, maxExtract = 0, energy = 0;

        public Builder(Consumer<SyncedEnergyStorage> syncFunction) {
            this.syncFunction = syncFunction;
        }

        public Builder(SyncedBlockEntity blockEntity) {
            this.syncFunction = ignored -> blockEntity.sync();
        }

        public Builder capacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder extractable(int amount) {
            this.maxExtract = amount;
            return this;
        }

        public Builder notReceiveable() {
            this.maxReceive = 0;
            return this;
        }

        public Builder maxReceive(int amount) {
            this.maxReceive = amount;
            return this;
        }

        public Builder defaultEnergy(int energy) {
            this.energy = energy;
            return this;
        }

        public SyncedEnergyStorage build() {
            return new SyncedEnergyStorage(
                    this.capacity,
                    this.maxReceive,
                    this.maxExtract,
                    this.energy,
                    this.syncFunction);
        }
    }
}
