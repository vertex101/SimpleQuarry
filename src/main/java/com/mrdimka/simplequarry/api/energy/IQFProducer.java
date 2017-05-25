package com.mrdimka.simplequarry.api.energy;

import net.minecraft.util.EnumFacing;

public interface IQFProducer extends IQFConnection
{
	/**
	 * Extracts Quarry Flux to side
	 * @param to - the side to give power to
	 * @param howMuch - how much QF should be given
	 * @param simulate - should this operation be simulated
	 * @return how much QF was removed from storage
	 **/
	public double produceQF(EnumFacing to, double howMuch, boolean simulate);
}