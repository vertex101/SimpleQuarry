package com.zeitheron.simplequarry.items;

import com.zeitheron.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants.NBT;

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
	
	public void tick(TilePoweredQuarry quarry, int index)
	{
	}
	
	public static boolean hasUpgrade(TilePoweredQuarry quarry, ItemUpgrade upgrade)
	{
		for(ItemUpgrade up : quarry.getUpgrades())
			if(up == upgrade)
				return true;
		return false;
	}
}