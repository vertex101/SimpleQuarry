package com.mrdimka.simplequarry.init;

import net.minecraft.block.Block;

import com.mrdimka.simplequarry.blocks.BlockFuelQuarry;
import com.mrdimka.simplequarry.blocks.BlockPoweredQuarry;
import com.mrdimka.simplequarry.cfg.ModConfig;

public class BlocksSQ
{
	public static final Block
							fuel_quarry = new BlockFuelQuarry(),
							powered_quarry;
	
	static
	{
		if(ModConfig.POWERED_QUARRY_RECIPE == -1)
			powered_quarry = null;
		else
			powered_quarry = new BlockPoweredQuarry();
	}
}