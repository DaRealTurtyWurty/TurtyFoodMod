package dev.turtywurty.turtyfoodmod.block.entity.util;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;

public class InvalidatingEnergyStorage extends ModifiableEnergyStorage {
    private final LazyOptional<EnergyStorage> lazyOptional = LazyOptional.of(() -> this);

    public InvalidatingEnergyStorage(int capacity) {
        super(capacity);
    }

    public InvalidatingEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public InvalidatingEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public InvalidatingEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public LazyOptional<EnergyStorage> getLazyOptional() {
        return this.lazyOptional;
    }

    public void invalidate() {
        this.lazyOptional.invalidate();
    }
}
