package com.zeitheron.simplequarry.gui.s;

import com.zeitheron.simplequarry.items.ItemUpgrade;
import com.zeitheron.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUpgrade extends Slot
{
	final TilePoweredQuarry quarry;
	
	public SlotUpgrade(IInventory inventoryIn, int index, int xPosition, int yPosition, TilePoweredQuarry quarry)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.quarry = quarry;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		if(!stack.isEmpty() && stack.getItem() instanceof ItemUpgrade)
			return ((ItemUpgrade) stack.getItem()).isCompatible(quarry);
		return false;
	}
}