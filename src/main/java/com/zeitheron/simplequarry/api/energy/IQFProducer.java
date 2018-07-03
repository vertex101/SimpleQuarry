package com.zeitheron.simplequarry.api.energy;

import net.minecraft.util.EnumFacing;

public interface IQFProducer extends IQFConnection
{
	public double produceQF(EnumFacing to, double quant, boolean simulate);
}