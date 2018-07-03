package com.zeitheron.simplequarry.cfg;

import com.zeitheron.hammercore.cfg.HCModConfigurations;
import com.zeitheron.hammercore.cfg.IConfigReloadListener;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyFloat;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyInt;
import com.zeitheron.simplequarry.InfoSQ;

import net.minecraftforge.common.config.Configuration;

@HCModConfigurations(modid = InfoSQ.MOD_ID)
public class ConfigsSQ implements IConfigReloadListener
{
	@ModConfigPropertyInt(name = "Powered Quarry Recipe", category = "Gameplay", defaultValue = 1, min = -1, max = 1, comment = "What recipe sould be used for Powered Quarry?\n-1 - disable quarry at all\n0 - 4 eyes of ender & normal chest\n1 - podzol, sea lantern, slime block, magma block & ender chest")
	public static int POWERED_QUARRY_RECIPE;
	
	@ModConfigPropertyFloat(name = "Blocks Per Coal", category = "Gameplay", defaultValue = 96F, min = 1F, max = 65536F, comment = "How much blocks can 1 coal mine? This value is taken for all other fuel types as a standart.")
	public static float BLOCKS_PER_COAL;
	
	public static Configuration cfgs;
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		ConfigsSQ.cfgs = cfgs;
	}
}