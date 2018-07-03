package com.zeitheron.simplequarry.tile;

import com.zeitheron.hammercore.internal.blocks.base.IBlockEnableable;
import com.zeitheron.hammercore.internal.capabilities.CapabilityEJ;
import com.zeitheron.hammercore.utils.energy.IPowerContainerItem;
import com.zeitheron.hammercore.utils.energy.IPowerStorage;
import com.zeitheron.hammercore.utils.inventory.InventoryDummy;
import com.zeitheron.simplequarry.api.energy.QFStorage;
import com.zeitheron.simplequarry.gui.c.ContainerPoweredQuarry;
import com.zeitheron.simplequarry.gui.g.GuiPoweredQuarry;
import com.zeitheron.simplequarry.init.BlocksSQ;
import com.zeitheron.simplequarry.init.ItemsSQ;
import com.zeitheron.simplequarry.items.ItemUpgrade;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TilePoweredQuarry extends TileFuelQuarry implements IEnergyStorage, IPowerStorage
{
	public InventoryDummy invUpgrades = new InventoryDummy(5);
	public NBTTagCompound additionalTags = new NBTTagCompound();
	
	public TilePoweredQuarry()
	{
		this.storage = new QFStorage(256000.0);
		this.tickRate = 5;
	}
	
	@Override
	public float getUsageMult()
	{
		float val = super.getUsageMult();
		
		for(int i = 0; i < 5; ++i)
			if(getUpgrade(i) != null)
				val *= getUpgrade(i).quarryUseMultiply;
			
		return val;
	}
	
	public ItemStack getUpgradeStack(int index)
	{
		return invUpgrades.getStackInSlot(index % 5);
	}
	
	public ItemUpgrade getUpgrade(int index)
	{
		ItemStack stack = getUpgradeStack(index);
		if(stack.isEmpty() || !(stack.getItem() instanceof ItemUpgrade))
			return null;
		return (ItemUpgrade) stack.getItem();
	}
	
	public ItemUpgrade[] getUpgrades()
	{
		ItemUpgrade[] upgrades = new ItemUpgrade[5];
		for(int i = 0; i < 5; ++i)
			upgrades[i] = getUpgrade(i);
		return upgrades;
	}
	
	@Override
	public void tick()
	{
		ItemStack stack = inv.getStackInSlot(0);
		
		if(stack.getItem() instanceof IPowerContainerItem)
		{
			IPowerContainerItem pc = (IPowerContainerItem) stack.getItem();
			int canExtract = pc.extractEnergy(stack, pc.getEnergyStored(stack), true);
			canExtract = Math.min(receiveEnergy(canExtract, true), canExtract);
			pc.extractEnergy(stack, canExtract, false);
			receiveEnergy(canExtract, false);
		} else if(stack.hasCapability(CapabilityEnergy.ENERGY, null))
		{
			IEnergyStorage pc = stack.getCapability(CapabilityEnergy.ENERGY, null);
			int canExtract = pc.extractEnergy(pc.getEnergyStored(), true);
			canExtract = Math.min(receiveEnergy(canExtract, true), canExtract);
			pc.extractEnergy(canExtract, false);
			receiveEnergy(canExtract, false);
		}
		
		for(int i = 0; i < 5; ++i)
		{
			ItemUpgrade up = getUpgrade(i);
			if(up != null && !up.canStay(this, i))
			{
				ItemStack s = invUpgrades.getStackInSlot(i).copy();
				invUpgrades.setInventorySlotContents(i, ItemStack.EMPTY);
				queueItems.add(s);
			} else if(up != null)
				up.tick(this, i);
		}
		
		super.tick();
		
		IBlockState state0 = world.getBlockState(pos);
		
		if(!world.isRemote && state0.getBlock() == BlocksSQ.powered_quarry && state0.getValue(IBlockEnableable.ENABLED) && y < 1)
		{
			world.setBlockState(pos, state0.withProperty(IBlockEnableable.ENABLED, false));
			validate();
			world.setTileEntity(pos, this);
		}
	}
	
	public boolean isDone()
	{
		IBlockState state0 = world.getBlockState(pos);
		if(!world.isRemote && state0.getBlock() == BlocksSQ.powered_quarry)
			return !state0.getValue(IBlockEnableable.ENABLED);
		return false;
	}
	
	@Override
	public boolean isMining(IBlockState state)
	{
		return state.getBlock() == BlocksSQ.powered_quarry && state.getValue(IBlockEnableable.ENABLED) && y < 1 && storage.storedQF > 0;
	}
	
	public int getFortune()
	{
		int fortune = 0;
		
		if(ItemUpgrade.hasUpgrade(this, ItemsSQ.UPGRADE_FORTUNE1))
			++fortune;
		if(ItemUpgrade.hasUpgrade(this, ItemsSQ.UPGRADE_FORTUNE2) && fortune == 1)
			++fortune;
		if(ItemUpgrade.hasUpgrade(this, ItemsSQ.UPGRADE_FORTUNE3) && fortune == 2)
			++fortune;
		
		return fortune;
	}
	
	@Override
	public NonNullList<ItemStack> makeDrops(BlockPos pos, IBlockState state)
	{
		NonNullList<ItemStack> drops = NonNullList.create();
		state.getBlock().getDrops(drops, world, pos, state, getFortune());
		for(int i = 0; i < 5; ++i)
			if(getUpgrade(i) != null)
				getUpgrade(i).handleDrops(this, pos, drops);
		return drops;
	}
	
	@Override
	public void addQueueItem(ItemStack e)
	{
		if(e.isEmpty())
			return;
		
		for(int i = 0; i < 5; ++i)
		{
			ItemUpgrade up = getUpgrade(i);
			if(up != null)
				try
				{
					e = up.handlePickup(e, this, i);
				} catch(Throwable err)
				{
				}
		}
		
		if(!e.isEmpty())
			super.addQueueItem(e);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		invUpgrades.readFromNBT(nbt.getCompoundTag("InventoryUpgrades"));
		additionalTags = nbt.getCompoundTag("AdditionalTags");
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		super.writeNBT(nbt);
		nbt.setTag("InventoryUpgrades", invUpgrades.writeToNBT(new NBTTagCompound()));
		nbt.setTag("AdditionalTags", additionalTags);
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiPoweredQuarry(player, this);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerPoweredQuarry(player, this);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		if(maxReceive >= 200)
			storage.consumeQF(null, maxReceive / 200, simulate);
		return maxReceive;
	}
	
	@Override
	public int getEnergyStored()
	{
		return 0;
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return 1000;
	}
	
	@Override
	public boolean canExtract()
	{
		return false;
	}
	
	@Override
	public boolean canReceive()
	{
		return true;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return 0;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(!(capability != CapabilityEnergy.ENERGY && capability != CapabilityEJ.ENERGY || facing != null && facing == EnumFacing.DOWN))
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(!(capability != CapabilityEnergy.ENERGY && capability != CapabilityEJ.ENERGY || facing != null && facing == EnumFacing.DOWN))
			return (T) this;
		return (T) super.getCapability(capability, facing);
	}
}