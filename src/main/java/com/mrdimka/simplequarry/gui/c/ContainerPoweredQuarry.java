package com.mrdimka.simplequarry.gui.c;

import com.mrdimka.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;

public class ContainerPoweredQuarry extends Container
{
	TilePoweredQuarry tile;
	
	public ContainerPoweredQuarry(TilePoweredQuarry tile, EntityPlayer player)
	{
		this.tile = tile;
		
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, 9 + j + i * 9, 8 + 18 * j, 84 + i * 18));
		
		addSlotToContainer(new SlotFurnaceFuel(tile.invFuel, 0, 25, 49));
		
//		for(int i = 0; i < 5; ++i)
//			addSlotToContainer(new Slot(tile.invUpgrades, i, 62 + i * 18, 59));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer arg0)
	{
		return arg0.getDistanceSq(tile.getPos()) <= 64D;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int s)
	{
		Slot slot = getSlot(s);
		
		if(slot.inventory == tile.invFuel && slot.getStack() != null)
			if(mergeItemStack(slot.getStack(), 0, 36, false))
				return slot.getStack();
		
		if(slot.inventory == p.inventory && slot.getStack() != null)
			if(mergeItemStack(slot.getStack(), 36, 37, false))
				return slot.getStack();
		
		return null;
	}
}