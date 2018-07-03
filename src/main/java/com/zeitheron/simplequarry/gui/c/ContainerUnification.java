package com.zeitheron.simplequarry.gui.c;

import com.zeitheron.hammercore.client.gui.impl.container.ItemTransferHelper.TransferableContainer;
import com.zeitheron.hammercore.utils.inventory.InventoryDummy;
import com.zeitheron.simplequarry.gui.c.ContainerUnification.UnificationData;
import com.zeitheron.simplequarry.gui.s.SlotForFilter;
import com.zeitheron.simplequarry.items.ItemUnificationUpgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerUnification extends TransferableContainer<UnificationData>
{
	public ContainerUnification(EntityPlayer player, ItemStack filter)
	{
		super(player, new UnificationData(filter), 8, 84);
	}
	
	@Override
	protected void addCustomSlots()
	{
		addSlotToContainer(new SlotForFilter(t.inventory, 0, 80, 49, () -> t.getStack()));
	}
	
	@Override
	protected void addInventorySlots(EntityPlayer player, int x, int y)
	{
		int start = inventorySlots.size();
		
		for(int i = 0; i < 9; ++i)
			if(!ItemUnificationUpgrade.isUnificationUpgrade(player.inventory.getStackInSlot(i)))
				addSlotToContainer(new Slot(player.inventory, i, x + i * 18, 58 + y));
			
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 9; ++j)
				if(!ItemUnificationUpgrade.isUnificationUpgrade(player.inventory.getStackInSlot(9 + j + i * 9)))
					addSlotToContainer(new Slot(player.inventory, 9 + j + i * 9, x + 18 * j, y + i * 18));
				
		transfer.setInventorySlots(start, inventorySlots.size());
	}
	
	@Override
	protected void addTransfer()
	{
		transfer.toInventory(0).addInTransferRule(0, getSlot(0)::isItemValid);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		t.getStack();
		super.onContainerClosed(playerIn);
	}
	
	public static class UnificationData
	{
		private ItemStack stack;
		public InventoryDummy inventory = new InventoryDummy(1);
		
		public UnificationData(ItemStack stack)
		{
			this.stack = stack;
			
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt != null)
				inventory.setInventorySlotContents(0, new ItemStack(nbt.getCompoundTag("Filter")));
		}
		
		public ItemStack getStack()
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt == null)
				nbt = new NBTTagCompound();
			nbt.setTag("Filter", inventory.getStackInSlot(0).serializeNBT());
			stack.setTagCompound(nbt);
			return stack;
		}
	}
}