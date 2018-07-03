package com.zeitheron.simplequarry.gui.s;

import com.zeitheron.simplequarry.init.ItemsSQ;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotForFilter extends Slot
{
	public Runnable save;
	
	public SlotForFilter(IInventory inventoryIn, int index, int xPosition, int yPosition, Runnable save)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.save = save;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack.getItem() == ItemsSQ.UPGRADE_FILTER;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		return true;
	}
	
	@Override
	public void onSlotChanged()
	{
		super.onSlotChanged();
		if(save != null)
			save.run();
	}
}