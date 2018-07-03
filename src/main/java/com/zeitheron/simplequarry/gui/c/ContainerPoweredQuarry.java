package com.zeitheron.simplequarry.gui.c;

import com.zeitheron.hammercore.client.gui.impl.container.ItemTransferHelper.TransferableContainer;
import com.zeitheron.simplequarry.gui.s.SlotFuelAndBattery;
import com.zeitheron.simplequarry.gui.s.SlotUpgrade;
import com.zeitheron.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.entity.player.EntityPlayer;

public class ContainerPoweredQuarry extends TransferableContainer<TilePoweredQuarry>
{
	public ContainerPoweredQuarry(EntityPlayer player, TilePoweredQuarry tile)
	{
		super(player, tile, 8, 84);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer arg0)
	{
		return arg0.getDistanceSq(t.getPos()) <= 64.0;
	}
	
	@Override
	protected void addCustomSlots()
	{
		addSlotToContainer(new SlotFuelAndBattery(t.inv, 0, 25, 49));
		for(int i = 0; i < 5; ++i)
			addSlotToContainer(new SlotUpgrade(t.invUpgrades, i, 62 + i * 18, 59, t));
	}
	
	@Override
	protected void addTransfer()
	{
		transfer.toInventory(0).addInTransferRule(0, getSlot(0)::isItemValid);
		for(int i = 0; i < 5; ++i)
			transfer.toInventory(1 + i).addInTransferRule(1, getSlot(1 + i)::isItemValid);
	}
}