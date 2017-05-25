package com.mrdimka.simplequarry.api.energy;

/**
 * This class allows you to convert QF between different power systems
 **/
public class UniversalConverter
{
	/**
	 * Converts [IC2]EnergyUnit into QuarryFlux
	 **/
	public static double EU_QF(double EU)
	{
		return EU / 2D;
	}
	
	/**
	 * Converts QuarryFlux into [IC2]EnergyUnit
	 **/
	public static double QF_EU(double QF)
	{
		return QF * 2D;
	}
	
	/**
	 * Converts [TE]RedstoneFlux into QuarryFlux
	 **/
	public static double RF_QF(double RF)
	{
		return RF / 8D;
	}
	
	/**
	 * Converts QuarryFlux into [TE]RedstoneFlux
	 **/
	public static double QF_RF(double QF)
	{
		return QF * 8D;
	}
	
	/**
	 * Converts [TS]TeslaUnit into QuarryFlux
	 **/
	public static double TU_QF(double TU)
	{
		return TU / 8D;
	}
	
	/**
	 * Converts QuarryFlux into [TS]TeslaUnit
	 **/
	public static double QF_TU(double QF)
	{
		return QF / 8D;
	}
	
	/**
	 * Converts Fuel Ticks into QuarryFlux
	 **/
	public static double FT_QF(double fuel)
	{
		return fuel * 2.5;
	}
	
	/**
	 * Converts QuarryFlux into Fuel Ticks
	 **/
	public static double QF_FT(double fuel)
	{
		return fuel / 2.5;
	}
}