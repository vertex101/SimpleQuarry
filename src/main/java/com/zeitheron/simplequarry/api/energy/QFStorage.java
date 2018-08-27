package com.zeitheron.simplequarry.api.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class QFStorage implements IQFConsumer, IQFProducer
{
	public double storedQF = 0.0;
	public double capacity = 1.0;
	
	public QFStorage(double capacity)
	{
		this.capacity = capacity;
	}
	
	public QFStorage(double capacity, double QF)
	{
		this(capacity);
		this.storedQF = Math.min(capacity, QF);
		if(Double.isInfinite(this.storedQF) || Double.isNaN(this.storedQF))
			this.storedQF = 0F;
	}
	
	@Override
	public boolean canConnectQF(EnumFacing to)
	{
		return true;
	}
	
	@Override
	public double getStoredQF(EnumFacing to)
	{
		if(Double.isInfinite(this.storedQF) || Double.isNaN(this.storedQF))
			this.storedQF = 0F;
		return this.storedQF;
	}
	
	@Override
	public double getQFCapacity(EnumFacing to)
	{
		return this.capacity;
	}
	
	@Override
	public double produceQF(EnumFacing to, double howMuch, boolean simulate)
	{
		if(Double.isInfinite(this.storedQF) || Double.isNaN(this.storedQF))
			this.storedQF = 0F;
		double extracted = Math.min(howMuch, this.storedQF);
		if(!simulate)
			this.storedQF -= extracted;
		return extracted;
	}
	
	@Override
	public double consumeQF(EnumFacing from, double howMuch, boolean simulate)
	{
		if(Double.isInfinite(this.storedQF) || Double.isNaN(this.storedQF))
			this.storedQF = 0F;
		double accepted = Math.min(this.capacity - this.storedQF, howMuch);
		if(!simulate)
			this.storedQF += accepted;
		return accepted;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(Double.isInfinite(this.storedQF) || Double.isNaN(this.storedQF))
			this.storedQF = 0F;
		nbt.setDouble("QFStored", this.storedQF);
		nbt.setDouble("QFCapacity", this.capacity);
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.storedQF = nbt.getDouble("QFStored");
		this.capacity = nbt.getDouble("QFCapacity");
		if(Double.isInfinite(this.storedQF) || Double.isNaN(this.storedQF))
			this.storedQF = 0F;
	}
	
	public static QFStorage readQFStorage(NBTTagCompound nbt)
	{
		return new QFStorage(nbt.getDouble("QFCapacity"), nbt.getDouble("QFStored"));
	}
}