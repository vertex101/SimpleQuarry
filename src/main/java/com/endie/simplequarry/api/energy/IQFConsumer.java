package com.endie.simplequarry.api.energy;

import net.minecraft.util.EnumFacing;

public interface IQFConsumer extends IQFConnection
{
	public double consumeQF(EnumFacing to, double quant, boolean simulate);
}