package com.faojen.exoterra.capabilities;

import com.faojen.exoterra.blocks.superiorpowerbank.SuperiorPowerBankBE;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class SuperiorExoterraEnergyTank implements IEnergyStorage, INBTSerializable<CompoundTag> {
    private static final String KEY = "energy";
    private int energy;
    private int capacity;
    private int maxInOut = 1000000;
    private SuperiorPowerBankBE tile;

    public SuperiorExoterraEnergyTank(SuperiorPowerBankBE tile, int energy, int capacity) {
        this.energy = energy;
        this.capacity = capacity;
        this.tile = tile;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(KEY, this.energy);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.energy = nbt.getInt(KEY);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxInOut, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            this.tile.setChanged();
        }

        return energyReceived;
    }

    public int consumeEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, Math.min(this.maxInOut, maxExtract));

        if (!simulate)
            energy -= energyExtracted;

        return energyExtracted;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    // We don't use this method and thus we don't let other people use it either
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
    	 int energyExtracted = Math.min(energy, Math.min(this.maxInOut, maxExtract));
    	 
    	 if (!simulate)
             energy -= energyExtracted;

         return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public String toString() {
        return "CommonEnergyTank{" +
                "energy=" + energy +
                ", capacity=" + capacity +
                ", maxInOut=" + maxInOut +
                '}';
    }
}
