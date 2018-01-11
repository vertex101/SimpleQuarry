package com.endie.simplequarry.items;

import com.endie.simplequarry.init.ItemsSQ;
import com.endie.simplequarry.tile.TilePoweredQuarry;

public class ItemEfficiencyUpgrade extends ItemUpgrade
{
	private static final ItemEfficiencyUpgrade[] upgrades = new ItemEfficiencyUpgrade[3];
	
	public int lvl;
	
	public ItemEfficiencyUpgrade(int lvl, float save)
	{
		setUnlocalizedName("upgrade_efficiency_" + (lvl + 1));
		if(upgrades[lvl] == null)
			upgrades[lvl] = this;
		this.lvl = lvl;
		quarryUseMultiply = 1 / save;
	}
	
	@Override
	public boolean isCompatible(TilePoweredQuarry quarry)
	{
		if(lvl > 0 && !hasUpgrade(quarry, upgrades[lvl - 1]))
			return false;
		return !hasUpgrade(quarry, this);
	}
	
	@Override
	public boolean canStay(TilePoweredQuarry quarry, int index)
	{
		if(lvl > 0 && !hasUpgrade(quarry, upgrades[lvl - 1]))
			return false;
		return true;
	}
}