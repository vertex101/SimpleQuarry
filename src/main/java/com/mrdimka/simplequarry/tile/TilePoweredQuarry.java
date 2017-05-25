package com.mrdimka.simplequarry.tile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidRegistry;

import com.mrdimka.hammercore.common.inventory.InventoryNonTile;
import com.mrdimka.hammercore.common.utils.ItemInsertionUtil;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.mrdimka.simplequarry.api.energy.IQFConsumer;
import com.mrdimka.simplequarry.api.energy.QFStorage;
import com.mrdimka.simplequarry.api.energy.UniversalConverter;
import com.mrdimka.simplequarry.blocks.BlockFuelQuarry;
import com.mrdimka.simplequarry.gui.c.ContainerPoweredQuarry;
import com.mrdimka.simplequarry.gui.g.GuiPoweredQuarry;
import com.mrdimka.simplequarry.init.BlocksSQ;
import com.pengu.hammercore.net.utils.NetPropertyBool;

public class TilePoweredQuarry extends TileSyncableTickable implements IQFConsumer, IEnergyStorage
{
	public QFStorage storage = new QFStorage(256000);
	
	public InventoryNonTile invFuel = new InventoryNonTile(1);
	public InventoryNonTile invUpgrades = new InventoryNonTile(5);
	public InventoryNonTile invTemp = new InventoryNonTile(64);
	
	public int burnTicks = 0;
	public int totalBurnTicks = 0;
	public static double BPC = 96D;
	public int y = -1;
	
	public final NetPropertyBool collectDirt;
	public NetPropertyBool collectCobble;
	// public NetPropertyBool listMode = false;
	
	{
		collectDirt = new NetPropertyBool(this, true);
		collectCobble = new NetPropertyBool(this, true);
	}
	
	@Override
	public void tick()
	{
		if(world.isRemote)
			return;
		
		if(y == -1)
			y = pos.getY() - 1;
		int chunkX, chunkZ;
		
		IBlockState state0 = world.getBlockState(pos);
		boolean working = state0.getValue(BlockFuelQuarry.IS_MINING);
		if(state0.getBlock() == BlocksSQ.powered_quarry && working && y < 1)
		{
			world.setBlockState(pos, state0.withProperty(BlockFuelQuarry.IS_MINING, false));
			world.setTileEntity(pos, this);
		}
		
		{
			Chunk c = world.getChunkFromBlockCoords(pos);
			chunkX = c.x;
			chunkZ = c.z;
		}
		
		double QFPerBlock = UniversalConverter.FT_QF(1600) / BPC;
		
		if(storage.consumeQF(null, UniversalConverter.FT_QF(1D), true) == UniversalConverter.FT_QF(1D))
			if(!world.isRemote && atTickRate(4) && burnTicks < 1)
			{
				ItemStack stack = invFuel.getStackInSlot(0);
				if(!stack.isEmpty() && TileEntityFurnace.getItemBurnTime(stack) > 0)
				{
					burnTicks += TileEntityFurnace.getItemBurnTime(stack);
					totalBurnTicks = burnTicks;
					stack.shrink(1);
				}
			}
		
		if(burnTicks > 0)
		{
			for(int i = 0; i < Math.min(8, burnTicks); ++i)
			{
				burnTicks--;
				double qf = storage.consumeQF(null, UniversalConverter.FT_QF(1D), true);
				if(qf == UniversalConverter.FT_QF(1D))
					storage.consumeQF(null, qf, false);
			}
			
			sync();
		}
		
		if(y > 0 && atTickRate(5) && storage.getStoredQF(null) >= QFPerBlock)
		{
			boolean hasBrokenBlock = false;
			start: for(int x = 0; x < 16; ++x)
				for(int z = 0; z < 16; ++z)
				{
					BlockPos pos = new BlockPos(chunkX * 16 + x, y, chunkZ * 16 + z);
					Block bb = world.getBlockState(pos).getBlock();
					if(!hasBrokenBlock && !world.isAirBlock(pos) && FluidRegistry.lookupFluidForBlock(bb) == null && bb != Blocks.FLOWING_LAVA && bb != Blocks.FLOWING_WATER)
					{
						IBlockState state = world.getBlockState(pos);
						Block b = state.getBlock();
						if(b.getBlockHardness(state, world, pos) < 0F)
							continue;
						captureItems(b.getDrops(world, pos, state, 0));
						hasBrokenBlock = true;
						world.destroyBlock(pos, false);
						storage.produceQF(null, QFPerBlock, false);
						sync();
						break start;
					}
				}
			if(!hasBrokenBlock)
				y--;
		}
		
		AxisAlignedBB aabb = new AxisAlignedBB(chunkX * 16 - 1, 0, chunkZ * 16 - 1, chunkX * 16 + 17, pos.getY() + 1, chunkZ * 16 + 17);
		captureEntityItems(aabb);
		
		for(int i = 0; i < invTemp.getSizeInventory(); ++i)
		{
			ItemStack stack = invTemp.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE) && !collectCobble.get())
				invTemp.setInventorySlotContents(i, ItemStack.EMPTY);
			if(!stack.isEmpty() && stack.getItem() == Item.getItemFromBlock(Blocks.DIRT) && !collectDirt.get())
				invTemp.setInventorySlotContents(i, ItemStack.EMPTY);
			else if(!world.isRemote && !invTemp.getStackInSlot(i).isEmpty() && atTickRate(2))
			{
				for(EnumFacing f : EnumFacing.VALUES)
				{
					if(f == EnumFacing.DOWN)
						continue;
					if(invTemp.getStackInSlot(i).isEmpty())
						break;
					ItemInsertionUtil.transferItemsOutFinal(this, invTemp, f);
				}
			}
		}
	}
	
	public void captureItems(List<ItemStack> items)
	{
		for(int i = 0; i < invTemp.getSizeInventory(); ++i)
			if(!world.isRemote && invTemp.getStackInSlot(i).isEmpty())
			{
				for(int j = 0; j < Math.min(items.size(), 1); ++j)
				{
					ItemStack item = items.get(j);
					
					if(!item.isEmpty() && item.getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE) && !collectCobble.get())
						continue;
					else if(!item.isEmpty() && item.getItem() == Item.getItemFromBlock(Blocks.DIRT) && !collectDirt.get())
						continue;
					else if(item.getCount() > 0)
					{
						ItemStack stack = item.copy();
						stack.setCount(1);
						invTemp.setInventorySlotContents(i, stack);
						item.shrink(1);
					}
				}
			}
	}
	
	public void captureEntityItems(AxisAlignedBB aabb)
	{
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, aabb);
		for(int j = 0; j < items.size(); ++j)
		{
			EntityItem item = items.get(j);
			if(!item.getEntityItem().isEmpty() && item.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE) && !collectCobble.get())
				item.setDead();
			if(!item.getEntityItem().isEmpty() && item.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.DIRT) && !collectDirt.get())
				item.setDead();
		}
		
		items = world.getEntitiesWithinAABB(EntityItem.class, aabb);
		
		for(int i = 0; i < invTemp.getSizeInventory(); ++i)
			if(!world.isRemote && invTemp.getStackInSlot(i).isEmpty())
			{
				for(int j = 0; j < Math.min(items.size(), 1); ++j)
				{
					EntityItem item = items.get(j);
					if(item.getEntityItem().getCount() > 0)
					{
						ItemStack stack = item.getEntityItem().copy();
						stack.setCount(1);
						invTemp.setInventorySlotContents(i, stack);
						item.getEntityItem().shrink(1);
						if(item.getEntityItem().isEmpty())
							item.setDead();
					}
				}
			}
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		storage.readFromNBT(nbt);
		burnTicks = nbt.getInteger("BurnTicks");
		totalBurnTicks = nbt.getInteger("TotalBurnTicks");
		y = nbt.getInteger("MineY");
		invFuel.readFromNBT(nbt.getCompoundTag("InventoryFuel"));
		invUpgrades.readFromNBT(nbt.getCompoundTag("InventoryUpgrades"));
		// invItemList.readFromNBT(nbt.getCompoundTag("InventoryItemList"));
		invTemp.readFromNBT(nbt.getCompoundTag("InventoryTemp"));
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		storage.writeToNBT(nbt);
		nbt.setInteger("BurnTicks", burnTicks);
		nbt.setInteger("MineY", y);
		nbt.setInteger("TotalBurnTicks", totalBurnTicks);
		
		NBTTagCompound invTag = new NBTTagCompound();
		invFuel.writeToNBT(invTag);
		nbt.setTag("InventoryFuel", invTag);
		
		invTag = new NBTTagCompound();
		invUpgrades.writeToNBT(invTag);
		nbt.setTag("InventoryUpgrades", invTag);
		
		// invTag = new NBTTagCompound();
		// invItemList.writeToNBT(invTag);
		// nbt.setTag("InventoryItemList", invTag);
		
		invTag = new NBTTagCompound();
		invTemp.writeToNBT(invTag);
		nbt.setTag("InventoryTemp", invTag);
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiPoweredQuarry(this, player);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerPoweredQuarry(this, player);
	}
	
	@Override
	public boolean canConnectQF(EnumFacing to)
	{
		return to != EnumFacing.UP && to != EnumFacing.DOWN;
	}
	
	@Override
	public double getStoredQF(EnumFacing to)
	{
		return storage.getStoredQF(to);
	}
	
	@Override
	public double getQFCapacity(EnumFacing to)
	{
		return storage.getQFCapacity(to);
	}
	
	@Override
	public double consumeQF(EnumFacing from, double howMuch, boolean simulate)
	{
		return storage.consumeQF(from, howMuch, simulate);
	}
	
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		if(maxReceive >= 200)
			storage.consumeQF(null, maxReceive / 200, simulate);
		return maxReceive;
	}
	
	public int getEnergyStored()
	{
		return 0;
	}
	
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
		if(capability == CapabilityEnergy.ENERGY && (facing == null || facing.getAxis() != Axis.Y))
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY && (facing == null || facing.getAxis() != Axis.Y))
			return (T) this;
		return super.getCapability(capability, facing);
	}
}