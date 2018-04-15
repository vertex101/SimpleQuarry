package com.endie.simplequarry.api;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemInjector
{
	public static ItemStack inject(ItemStack item, TileEntity tile, EnumFacing capFace)
	{
		return tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, capFace) ? inject(item, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, capFace)) : item;
	}
	
	public static ItemStack inject(ItemStack item, IItemHandler h)
	{
		for(int i = 0; h != null && i < h.getSlots() && !item.isEmpty(); ++i)
			if(!item.isEmpty())
				item = h.insertItem(i, item, false);
		return item;
	}
}