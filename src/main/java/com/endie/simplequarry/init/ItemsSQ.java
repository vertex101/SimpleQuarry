package com.endie.simplequarry.init;

import com.endie.simplequarry.items.ItemAutoSmeltUpgrade;
import com.endie.simplequarry.items.ItemEfficiencyUpgrade;
import com.endie.simplequarry.items.ItemFilterUpgrade;
import com.endie.simplequarry.items.ItemFortuneUpgrade;
import com.endie.simplequarry.items.ItemSilkUpgrade;
import com.endie.simplequarry.items.ItemUnificationUpgrade;
import com.endie.simplequarry.items.ItemUpgrade;

import net.minecraft.item.Item;

public class ItemsSQ
{
	public static final Item UPGRADE_BASE = new Item().setUnlocalizedName("upgrade_base");
	public static final ItemUpgrade UPGRADE_FILTER = new ItemFilterUpgrade();
	public static final ItemUpgrade UPGRADE_UNIFICATION = new ItemUnificationUpgrade();
	public static final ItemUpgrade UPGRADE_SILK = new ItemSilkUpgrade();
	public static final ItemUpgrade UPGRADE_AUTO_SMELT = new ItemAutoSmeltUpgrade();
	public static final ItemUpgrade UPGRADE_FORTUNE1 = new ItemFortuneUpgrade(0);
	public static final ItemUpgrade UPGRADE_FORTUNE2 = new ItemFortuneUpgrade(1);
	public static final ItemUpgrade UPGRADE_FORTUNE3 = new ItemFortuneUpgrade(2);
	public static final ItemUpgrade UPGRADE_EFFICIENCY1 = new ItemEfficiencyUpgrade(0, 1.05F);
	public static final ItemUpgrade UPGRADE_EFFICIENCY2 = new ItemEfficiencyUpgrade(1, 1.1F);
	public static final ItemUpgrade UPGRADE_EFFICIENCY3 = new ItemEfficiencyUpgrade(2, 1.35F);
}