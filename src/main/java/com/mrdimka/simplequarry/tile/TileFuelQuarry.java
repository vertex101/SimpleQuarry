package com.mrdimka.simplequarry.tile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidRegistry;

import com.mrdimka.hammercore.common.inventory.InventoryNonTile;
import com.mrdimka.hammercore.common.utils.ItemInsertionUtil;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.mrdimka.simplequarry.api.energy.IQFConsumer;
import com.mrdimka.simplequarry.api.energy.QFStorage;
import com.mrdimka.simplequarry.api.energy.UniversalConverter;
import com.mrdimka.simplequarry.blocks.BlockFuelQuarry;
import com.mrdimka.simplequarry.gui.c.ContainerFuelQuarry;
import com.mrdimka.simplequarry.gui.g.GuiFuelQuarry;
import com.mrdimka.simplequarry.init.BlocksSQ;

public class TileFuelQuarry extends TileSyncableTickable implements IQFConsumer
{
	public static double BPC = 96D;
	
	public QFStorage storage = new QFStorage(8000);
	public InventoryNonTile inv = new InventoryNonTile(1);
	public InventoryNonTile invTemp = new InventoryNonTile(64);
	public int burnTicks = 0;
	public int totalBurnTicks = 0;
	public int y = -1;
	
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
		if(state0.getBlock() == BlocksSQ.fuel_quarry && working && y < 1)
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
		
		if(storage.consumeQF(null, UniversalConverter.FT_QF(1D), true) == UniversalConverter.FT_QF(1D) && working)
			if(!world.isRemote && atTickRate(20) && burnTicks < 1)
			{
				ItemStack stack = inv.getStackInSlot(0);
				if(!stack.isEmpty() && TileEntityFurnace.getItemBurnTime(stack) > 0)
				{
					burnTicks += TileEntityFurnace.getItemBurnTime(stack);
					totalBurnTicks = burnTicks;
					stack.shrink(1);
				}
			}
		
		if(burnTicks > 0)
		{
			burnTicks--;
			double qf = storage.consumeQF(null, UniversalConverter.FT_QF(1D), true);
			if(qf == UniversalConverter.FT_QF(1D))
				storage.consumeQF(null, qf, false);
			sync();
		}
		
		double qf = storage.getStoredQF(null);
		if(Double.isNaN(qf) || Double.isInfinite(qf)) storage.storedQF = 0;
		
		if(y > 0 && atTickRate(10) && storage.getStoredQF(null) >= QFPerBlock)
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
		captureEntityItems(world.getEntitiesWithinAABB(EntityItem.class, aabb));
		
		// for(int i = 0; i < invTemp.getSizeInventory(); ++i)
		// if(!world.isRemote && invTemp.getStackInSlot(i) == nul)
		// {
		// AxisAlignedBB aabb = new AxisAlignedBB(chunkX * 16 - 1, 0, chunkZ *
		// 16 - 1, chunkX * 16 + 17, pos.getY() + 1, chunkZ * 16 + 17);
		// List<EntityItem> items =
		// world.getEntitiesWithinAABB(EntityItem.class, aabb);
		// for(int j = 0; j < Math.min(items.size(), 1); ++j)
		// {
		// EntityItem item = items.get(j);
		// if(item.getEntityItem().stackSize > 0)
		// {
		// ItemStack stack = item.getEntityItem().copy();
		// stack.stackSize = 1;
		// invTemp.setInventorySlotContents(i, stack);
		// item.getEntityItem().stackSize--;
		// if(item.getEntityItem().stackSize < 1) item.setDead();
		// }
		// }
		// }
		
		for(int i = 0; i < invTemp.getSizeInventory(); ++i)
			if(!world.isRemote && !invTemp.getStackInSlot(i).isEmpty() && atTickRate(2))
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
	
	public void captureItems(List<ItemStack> items)
	{
		for(int i = 0; i < invTemp.getSizeInventory(); ++i)
			if(!world.isRemote && invTemp.getStackInSlot(i).isEmpty())
			{
				for(int j = 0; j < Math.min(items.size(), 1); ++j)
				{
					ItemStack item = items.get(j);
					if(!item.isEmpty())
					{
						ItemStack stack = item.copy();
						stack.setCount(1);
						invTemp.setInventorySlotContents(i, stack);
						item.shrink(1);
					}
				}
			}
	}
	
	public void captureEntityItems(List<EntityItem> items)
	{
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
						if(item.getEntityItem().getCount() < 1)
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
		inv.readFromNBT(nbt.getCompoundTag("Inventory"));
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
		inv.writeToNBT(invTag);
		nbt.setTag("Inventory", invTag);
		
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
		return new GuiFuelQuarry(this, player);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerFuelQuarry(this, player);
	}
	
	@Override
	public boolean canConnectQF(EnumFacing to)
	{
		return true;
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
}