package com.zeitheron.simplequarry.gui.s;

import com.zeitheron.hammercore.utils.energy.IPowerContainerItem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

public class SlotFuelAndBattery extends SlotFurnaceFuel
{
	public SlotFuelAndBattery(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	public boolean isItemValid(ItemStack stack)
	{
		if(stack.getItem() instanceof IPowerContainerItem || stack.hasCapability(CapabilityEnergy.ENERGY, null))
			return true;
		return super.isItemValid(stack);
	}
}
