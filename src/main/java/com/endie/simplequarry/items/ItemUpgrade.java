package com.endie.simplequarry.items;

import com.endie.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public abstract class ItemUpgrade extends Item
{
	public float quarryUseMultiply = 1;
	
	{
		setMaxStackSize(1);
	}
	
	public void handleDrops(TilePoweredQuarry quarry, BlockPos pos, NonNullList<ItemStack> drops)
	{
		
	}
	
	public boolean canStay(TilePoweredQuarry quarry, int index)
	{
		return true;
	}
	
	public ItemStack handlePickup(ItemStack stack, TilePoweredQuarry quarry, int index)
	{
		return stack;
	}
	
	public boolean isCompatible(TilePoweredQuarry quarry)
	{
		return true;
	}
	
	public static boolean hasUpgrade(TilePoweredQuarry quarry, ItemUpgrade upgrade)
	{
		for(ItemUpgrade up : quarry.getUpgrades())
			if(up == upgrade)
				return true;
		return false;
	}
}