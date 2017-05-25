package com.mrdimka.simplequarry.api.energy;

import net.minecraft.util.EnumFacing;

public interface IQFConsumer extends IQFConnection
{
	public double consumeQF(EnumFacing from, double howMuch, boolean simulate);
}