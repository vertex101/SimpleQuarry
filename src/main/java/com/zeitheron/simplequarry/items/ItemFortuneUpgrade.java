package com.zeitheron.simplequarry.items;

import com.zeitheron.simplequarry.init.ItemsSQ;
import com.zeitheron.simplequarry.tile.TilePoweredQuarry;

public class ItemFortuneUpgrade extends ItemUpgrade
{
	private static final ItemFortuneUpgrade[] upgrades = new ItemFortuneUpgrade[3];
	
	public int lvl;
	
	public ItemFortuneUpgrade(int lvl)
	{
		setUnlocalizedName("upgrade_fortune_" + (lvl + 1));
		if(upgrades[lvl] == null)
			upgrades[lvl] = this;
		this.lvl = lvl;
		quarryUseMultiply = 2;
	}
	
	@Override
	public boolean isCompatible(TilePoweredQuarry quarry)
	{
		if(lvl > 0 && !hasUpgrade(quarry, upgrades[lvl - 1]))
			return false;
		return !hasUpgrade(quarry, this) && !hasUpgrade(quarry, ItemsSQ.UPGRADE_SILK);
	}
	
	@Override
	public boolean canStay(TilePoweredQuarry quarry, int index)
	{
		if(lvl > 0 && !hasUpgrade(quarry, upgrades[lvl - 1]))
			return false;
		return !hasUpgrade(quarry, ItemsSQ.UPGRADE_SILK);
	}
}