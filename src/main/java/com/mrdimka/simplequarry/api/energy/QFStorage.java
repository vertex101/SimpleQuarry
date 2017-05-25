package com.mrdimka.simplequarry.api.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class QFStorage implements IQFConsumer, IQFProducer
{
	public double storedQF = 0D;
	public double capacity = 1D;
	
	public QFStorage(double capacity)
	{
		this.capacity = capacity;
	}
	
	public QFStorage(double capacity, double QF)
	{
		this(capacity);
		storedQF = Math.min(capacity, QF);
	}
	
	@Override
	public boolean canConnectQF(EnumFacing to)
	{
		return true;
	}

	@Override
	public double getStoredQF(EnumFacing to)
	{
		return storedQF;
	}

	@Override
	public double getQFCapacity(EnumFacing to)
	{
		return capacity;
	}

	@Override
	public double produceQF(EnumFacing to, double howMuch, boolean simulate)
	{
		double extracted = Math.min(howMuch, storedQF);
		if(!simulate) storedQF -= extracted;
		return extracted;
	}

	@Override
	public double consumeQF(EnumFacing from, double howMuch, boolean simulate)
	{
		double accepted = Math.min(capacity - storedQF, howMuch);
		if(!simulate) storedQF += accepted;
		return accepted;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("QFStored", storedQF);
		nbt.setDouble("QFCapacity", capacity);
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		storedQF = nbt.getDouble("QFStored");
		capacity = nbt.getDouble("QFCapacity");
	}
	
	public static QFStorage readQFStorage(NBTTagCompound nbt)
	{
		return new QFStorage(nbt.getDouble("QFCapacity"), nbt.getDouble("QFStored"));
	}
}