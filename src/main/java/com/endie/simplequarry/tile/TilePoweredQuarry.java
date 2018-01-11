package com.endie.simplequarry.tile;

import com.endie.simplequarry.api.energy.QFStorage;
import com.endie.simplequarry.blocks.BlockFuelQuarry;
import com.endie.simplequarry.gui.c.ContainerPoweredQuarry;
import com.endie.simplequarry.gui.g.GuiPoweredQuarry;
import com.endie.simplequarry.init.BlocksSQ;
import com.endie.simplequarry.init.ItemsSQ;
import com.endie.simplequarry.items.ItemUpgrade;
import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.common.inventory.InventoryDummy;
import com.pengu.hammercore.energy.iPowerContainerItem;
import com.pengu.hammercore.energy.iPowerStorage;

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

public class TilePoweredQuarry extends TileFuelQuarry implements IEnergyStorage, iPowerStorage
{
	public InventoryDummy invUpgrades = new InventoryDummy(5);
	
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
		
		if(stack.getItem() instanceof iPowerContainerItem)
		{
			iPowerContainerItem pc = (iPowerContainerItem) stack.getItem();
			int canExtract = pc.extractEnergy(stack, pc.getEnergyStored(stack), true);
			canExtract = Math.min(this.receiveEnergy(canExtract, true), canExtract);
			pc.extractEnergy(stack, canExtract, false);
			receiveEnergy(canExtract, false);
		} else if(stack.hasCapability(CapabilityEnergy.ENERGY, null))
		{
			IEnergyStorage pc = stack.getCapability(CapabilityEnergy.ENERGY, null);
			int canExtract = pc.extractEnergy(pc.getEnergyStored(), true);
			canExtract = Math.min(this.receiveEnergy(canExtract, true), canExtract);
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
			}
		}
		
		super.tick();
		
		IBlockState state0 = world.getBlockState(pos);
		
		if(!world.isRemote && state0.getBlock() == BlocksSQ.powered_quarry && state0.getValue(BlockFuelQuarry.IS_MINING) && y < 1)
		{
			this.world.setBlockState(pos, state0.withProperty(BlockFuelQuarry.IS_MINING, false));
			this.world.setTileEntity(pos, this);
		}
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
		for(int i = 0; i < 5; ++i)
		{
			ItemUpgrade up = getUpgrade(i);
			if(up != null)
				e = up.handlePickup(e, this, i);
		}
		
		if(!e.isEmpty())
			super.addQueueItem(e);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		this.invUpgrades.readFromNBT(nbt.getCompoundTag("InventoryUpgrades"));
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		super.writeNBT(nbt);
		nbt.setTag("InventoryUpgrades", invUpgrades.writeToNBT(new NBTTagCompound()));
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
			this.storage.consumeQF(null, maxReceive / 200, simulate);
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