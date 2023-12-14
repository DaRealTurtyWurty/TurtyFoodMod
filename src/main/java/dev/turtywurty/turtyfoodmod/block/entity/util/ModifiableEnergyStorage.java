package dev.turtywurty.turtyfoodmod.block.entity.util;

import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraftforge.energy.EnergyStorage;

public class ModifiableEnergyStorage extends EnergyStorage {
    public ModifiableEnergyStorage(int capacity) {
        super(capacity);
    }

    public ModifiableEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public ModifiableEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public ModifiableEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergy(int energy) {
        this.energy = Mth.clamp(energy, 0, getMaxEnergyStored());
        onEnergyChanged();
    }

    public void takeEnergy(int energy) {
        setEnergy(getEnergyStored() - energy);
    }

    public void giveEnergy(int energy) {
        setEnergy(getEnergyStored() + energy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = super.receiveEnergy(maxReceive, simulate);
        if (energy > 0 && !simulate) {
            onEnergyChanged();
        }

        return energy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = super.extractEnergy(maxExtract, simulate);
        if (energy > 0 && !simulate) {
            onEnergyChanged();
        }

        return energy;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        int energy = this.energy;

        super.deserializeNBT(nbt);

        if (energy != this.energy) {
            onEnergyChanged();
        }
    }

    public void onEnergyChanged() {}
}
