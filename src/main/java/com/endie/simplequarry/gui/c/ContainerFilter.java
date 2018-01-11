package com.endie.simplequarry.gui.c;

import org.apache.commons.lang3.ArrayUtils;

import com.endie.simplequarry.gui.c.ContainerFilter.FilterData;
import com.endie.simplequarry.gui.s.SlotGhost;
import com.endie.simplequarry.items.ItemFilterUpgrade;
import com.pengu.hammercore.common.inventory.InventoryDummy;
import com.pengu.hammercore.core.gui.container.ItemTransferHelper.TransferableContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class ContainerFilter extends TransferableContainer<FilterData>
{
	public ContainerFilter(EntityPlayer player, ItemStack filter)
	{
		super(player, new FilterData(filter), 8, 84);
	}
	
	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id)
	{
		if(id == 0)
		{
			t.invert = !t.invert;
			t.getStack();
			
			return true;
		}
		
		if(id == 1)
		{
			t.useod = !t.useod;
			t.getStack();
			
			return true;
		}
		
		if(id == 2)
		{
			t.usemeta = !t.usemeta;
			t.getStack();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void addCustomSlots()
	{
		for(int x = 0; x < 4; ++x)
			for(int y = 0; y < 3; ++y)
				addSlotToContainer(new SlotGhost(t.inventory, x + y * 4, 53 + x * 18, 17 + y * 18, () -> t.getStack()));
	}
	
	@Override
	protected void addInventorySlots(EntityPlayer player, int x, int y)
	{
		int start = inventorySlots.size();
		
		for(int i = 0; i < 9; ++i)
			if(!ItemFilterUpgrade.isFilterUpgrade(player.inventory.getStackInSlot(i)))
				addSlotToContainer(new Slot(player.inventory, i, x + i * 18, 58 + y));
			
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 9; ++j)
				if(!ItemFilterUpgrade.isFilterUpgrade(player.inventory.getStackInSlot(9 + j + i * 9)))
					addSlotToContainer(new Slot(player.inventory, 9 + j + i * 9, x + 18 * j, y + i * 18));
				
		transfer.setInventorySlots(start, inventorySlots.size());
	}
	
	@Override
	protected void addTransfer()
	{
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		t.getStack();
		super.onContainerClosed(playerIn);
	}
	
	public static class FilterData
	{
		private ItemStack stack;
		public InventoryDummy inventory = new InventoryDummy(12);
		
		public boolean invert;
		public boolean useod;
		public boolean usemeta;
		public boolean ignorenbt;
		
		public FilterData(ItemStack stack)
		{
			this.stack = stack;
			
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt != null)
			{
				invert = nbt.getBoolean("InvertList");
				useod = nbt.getBoolean("OreDictionary");
				usemeta = nbt.getBoolean("Metadata");
				ignorenbt = nbt.getBoolean("IgnoreNBT");
				
				NBTTagList items = nbt.getTagList("Filter", NBT.TAG_COMPOUND);
				
				for(int i = 0; i < Math.min(inventory.getSizeInventory(), items.tagCount()); ++i)
					inventory.setInventorySlotContents(i, new ItemStack(items.getCompoundTagAt(i)));
			}
		}
		
		public ItemStack getStack()
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt == null)
				nbt = new NBTTagCompound();
			
			nbt.setBoolean("InvertList", invert);
			nbt.setBoolean("OreDictionary", useod);
			nbt.setBoolean("Metadata", usemeta);
			nbt.setBoolean("IgnoreNBT", ignorenbt);
			
			NBTTagList items = new NBTTagList();
			for(int i = 0; i < inventory.getSizeInventory(); ++i)
				items.appendTag(inventory.getStackInSlot(i).serializeNBT());
			nbt.setTag("Filter", items);
			stack.setTagCompound(nbt);
			return stack;
		}
	}
}