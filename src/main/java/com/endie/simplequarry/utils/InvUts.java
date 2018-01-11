package com.endie.simplequarry.utils;

import javax.annotation.Nullable;

import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.common.utils.WorldUtil;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class InvUts
{
	public static int queryItemOrEmptySlot(IInventory inv, ItemStack stack, boolean checkCanInsert, @Nullable EnumFacing from)
	{
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			if((checkCanInsert || !InvUts.stacksEqual(stack, inv.getStackInSlot(i)) || inv.getStackInSlot(i).getCount() <= Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit())) && (!checkCanInsert || !InvUts.canInsert(inv, i, stack, from)))
				continue;
			return i;
		}
		return -1;
	}
	
	public static boolean putItem(IInventory inv, int slot, ItemStack stack)
	{
		if(inv.getSizeInventory() <= slot)
			return false;
		if(inv.getStackInSlot(slot).isEmpty())
		{
			inv.setInventorySlotContents(slot, stack.copy());
			stack.setCount(0);
			return true;
		}
		if(InvUts.stacksEqual(stack, inv.getStackInSlot(slot)) && inv.getStackInSlot(slot).getCount() < Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit()))
		{
			int put = Math.min(stack.getCount(), stack.getMaxStackSize() - inv.getStackInSlot(slot).getCount());
			inv.getStackInSlot(slot).grow(put);
			stack.shrink(put);
		}
		return false;
	}
	
	public static boolean canInsert(IInventory inv, int slot, ItemStack stack, @Nullable EnumFacing from)
	{
		if(inv.getSizeInventory() <= slot)
			return false;
		ISidedInventory sided = (ISidedInventory) WorldUtil.cast((Object) inv, ISidedInventory.class);
		if(sided != null && !sided.canInsertItem(slot, stack, from) || sided == null && !inv.isItemValidForSlot(slot, stack))
			return false;
		if(inv.getStackInSlot(slot).isEmpty())
			return true;
		if(InvUts.stacksEqual(stack, inv.getStackInSlot(slot)) && inv.getStackInSlot(slot).getCount() < Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit()))
			return true;
		return false;
	}
	
	public static boolean stacksEqual(ItemStack a, ItemStack b)
	{
		if(a == b || a.isEmpty() && b.isEmpty())
			return true;
		if(!InterItemStack.isStackNull((ItemStack) a) && !InterItemStack.isStackNull((ItemStack) b) && a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage() && (a.hasTagCompound() && b.hasTagCompound() ? ItemStack.areItemStackTagsEqual((ItemStack) a, (ItemStack) b) : !a.hasTagCompound() && !b.hasTagCompound()))
			return true;
		return false;
	}
}
