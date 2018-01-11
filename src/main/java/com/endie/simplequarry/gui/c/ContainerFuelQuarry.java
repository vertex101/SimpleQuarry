package com.endie.simplequarry.gui.c;

import com.endie.simplequarry.tile.TileFuelQuarry;
import com.pengu.hammercore.core.gui.container.ItemTransferHelper.TransferableContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.SlotFurnaceFuel;

public class ContainerFuelQuarry extends TransferableContainer<TileFuelQuarry>
{
	public ContainerFuelQuarry(EntityPlayer player, TileFuelQuarry tile)
	{
		super(player, tile, 8, 84);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq(t.getPos()) <= 64.0;
	}
	
	@Override
	protected void addCustomSlots()
	{
		addSlotToContainer(new SlotFurnaceFuel(t.inv, 0, 80, 49));
	}
	
	@Override
	protected void addTransfer()
	{
		transfer.toInventory(0).addInTransferRule(0, getSlot(0)::isItemValid);
	}
}