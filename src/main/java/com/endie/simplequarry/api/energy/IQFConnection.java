package com.endie.simplequarry.api.energy;

import net.minecraft.util.EnumFacing;

public interface IQFConnection
{
	public boolean canConnectQF(EnumFacing to);
	
	public double getStoredQF(EnumFacing to);
	
	public double getQFCapacity(EnumFacing to);
}