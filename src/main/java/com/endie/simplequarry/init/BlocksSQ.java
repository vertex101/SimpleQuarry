package com.endie.simplequarry.init;

import com.endie.simplequarry.blocks.BlockFuelQuarry;
import com.endie.simplequarry.blocks.BlockPoweredQuarry;
import com.endie.simplequarry.cfg.ConfigsSQ;

import net.minecraft.block.Block;

public class BlocksSQ
{
	public static final Block fuel_quarry = new BlockFuelQuarry();
	public static final Block powered_quarry = ConfigsSQ.POWERED_QUARRY_RECIPE == -1 ? null : new BlockPoweredQuarry();
}