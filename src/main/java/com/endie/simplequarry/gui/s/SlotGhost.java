package com.endie.simplequarry.gui.s;

import com.endie.simplequarry.init.ItemsSQ;
import com.pengu.hammercore.common.InterItemStack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGhost extends Slot
{
	public Runnable save;
	
	public SlotGhost(IInventory inventoryIn, int index, int xPosition, int yPosition, Runnable save)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.save = save;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		if(stack.getItem() == ItemsSQ.UPGRADE_FILTER)
			return false;
		
		ItemStack ghost = stack.copy();
		ghost.setCount(1);
		inventory.setInventorySlotContents(getSlotIndex(), ghost);
		
		if(save != null)
			save.run();
		
		return false;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		inventory.setInventorySlotContents(getSlotIndex(), InterItemStack.NULL_STACK);
		
		if(save != null)
			save.run();
		
		return false;
	}
}