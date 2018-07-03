package com.zeitheron.simplequarry.init;

import com.zeitheron.simplequarry.blocks.BlockFuelQuarry;
import com.zeitheron.simplequarry.blocks.BlockPoweredQuarry;
import com.zeitheron.simplequarry.cfg.ConfigsSQ;

import net.minecraft.block.Block;

public class BlocksSQ
{
	public static final Block fuel_quarry = new BlockFuelQuarry();
	public static final Block powered_quarry = ConfigsSQ.POWERED_QUARRY_RECIPE == -1 ? null : new BlockPoweredQuarry();
}