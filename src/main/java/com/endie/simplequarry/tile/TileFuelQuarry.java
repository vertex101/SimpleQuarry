package com.endie.simplequarry.tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.endie.simplequarry.SimpleQuarry;
import com.endie.simplequarry.api.energy.IQFConsumer;
import com.endie.simplequarry.api.energy.QFStorage;
import com.endie.simplequarry.api.energy.UniversalConverter;
import com.endie.simplequarry.blocks.BlockFuelQuarry;
import com.endie.simplequarry.cfg.ConfigsSQ;
import com.endie.simplequarry.gui.c.ContainerFuelQuarry;
import com.endie.simplequarry.gui.g.GuiFuelQuarry;
import com.endie.simplequarry.init.BlocksSQ;
import com.endie.simplequarry.net.PacketBlock;
import com.endie.simplequarry.proxy.CommonProxy;
import com.endie.simplequarry.utils.InvUts;
import com.endie.simplequarry.vortex.QuarryVortex;
import com.pengu.hammercore.common.iWrenchable;
import com.pengu.hammercore.common.inventory.InventoryDummy;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.tile.iTileDroppable;
import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidRegistry;

public class TileFuelQuarry extends TileSyncableTickable implements IQFConsumer, iWrenchable, iTileDroppable
{
	public static Map<String, BlockPos> chunks = new HashMap<String, BlockPos>();
	public QFStorage storage = new QFStorage(8000);
	public InventoryDummy inv = new InventoryDummy(1);
	public NonNullList<ItemStack> queueItems = NonNullList.create();
	public int burnTicks = 0;
	public int totalBurnTicks = 0;
	public int y = -1;
	public AxisAlignedBB boundingBox;
	public QuarryVortex vortex;
	protected int tickRate = 10;
	
	public float getUsageMult()
	{
		return 1F;
	}
	
	@Override
	public void tick()
	{
		double qf;
		ItemStack stack;
		Chunk c = world.getChunkFromBlockCoords(pos);
		int chunkX = c.x;
		int chunkZ = c.z;
		if(world.isRemote)
		{
			if(boundingBox == null || boundingBox.minY != (double) y)
				boundingBox = new AxisAlignedBB(chunkX * 16, y, chunkZ * 16, chunkX * 16 + 16, pos.getY(), chunkZ * 16 + 16);
			if(vortex == null)
				vortex = new QuarryVortex(this);
			SimpleQuarry.proxy.addParticleVortex(vortex);
			return;
		}
		if(queueItems.size() >= 2)
		{
			tryEject();
			return;
		}
		if(y == -1)
		{
			y = pos.getY() - 1;
			boundingBox = new AxisAlignedBB(chunkX * 16, y, chunkZ * 16, chunkX * 16 + 16, y + 1, chunkZ * 16 + 16);
			sendChangesToNearby();
		}
		if(storage.storedQF > 0.0)
		{
			BlockPos cpos = chunks.get("" + chunkX + "|" + chunkZ);
			if(cpos != null && cpos.toLong() != pos.toLong() && world.getTileEntity(cpos) instanceof TileFuelQuarry)
			{
				loc.destroyBlock(true);
				world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3, true);
				return;
			}
			chunks.put("" + chunkX + "|" + chunkZ, pos);
		}
		IBlockState state0 = world.getBlockState(pos);
		
		if(state0.getBlock() == BlocksSQ.fuel_quarry && state0.getValue(BlockFuelQuarry.IS_MINING) && y < 1)
		{
			world.setBlockState(pos, state0.withProperty(BlockFuelQuarry.IS_MINING, false));
			world.setTileEntity(pos, create(world, serializeNBT()));
		}
		
		double QFPerBlock = UniversalConverter.FT_QF(TileEntityFurnace.getItemBurnTime(CommonProxy.COAL)) / (double) ConfigsSQ.BLOCKS_PER_COAL;
		
		if(storage.consumeQF(null, UniversalConverter.FT_QF(1), true) == UniversalConverter.FT_QF(1) && state0.getValue(BlockFuelQuarry.IS_MINING) && !world.isRemote && atTickRate(20) && burnTicks < 1 && !(stack = inv.getStackInSlot(0)).isEmpty() && TileEntityFurnace.getItemBurnTime(stack) > 0)
		{
			burnTicks += TileEntityFurnace.getItemBurnTime(stack);
			totalBurnTicks = burnTicks;
			stack.shrink(1);
			sendChangesToNearby();
		}
		
		if(burnTicks > 0)
		{
			--burnTicks;
			
			float mult = getUsageMult();
			
			double qf2 = storage.consumeQF(null, UniversalConverter.FT_QF(1 / mult), true);
			if(qf2 == UniversalConverter.FT_QF(1 / mult))
				storage.consumeQF(null, qf2, false);
			
			sendChangesToNearby();
		}
		
		if(Double.isNaN(qf = storage.getStoredQF(null)) || Double.isInfinite(qf))
			storage.storedQF = 0.0;
		
		if(y > 0 && atTickRate(tickRate) && storage.getStoredQF(null) >= QFPerBlock)
		{
			boolean hasBrokenBlock = false;
			block0: for(int x = 0; x < 16; ++x)
			{
				for(int z = 0; z < 16; ++z)
				{
					BlockPos pos = new BlockPos(chunkX * 16 + x, y, chunkZ * 16 + z);
					IBlockState state = world.getBlockState(pos);
					Block b = state.getBlock();
					if(hasBrokenBlock || world.isAirBlock(pos) || FluidRegistry.lookupFluidForBlock((Block) b) != null || b == Blocks.FLOWING_LAVA || b == Blocks.FLOWING_WATER || !canBreak(state, pos))
						continue;
					captureItems(makeDrops(pos, state));
					hasBrokenBlock = true;
					breakBlock(pos, state);
					storage.produceQF(null, QFPerBlock, false);
					sendChangesToNearby();
					break block0;
				}
			}
			if(!hasBrokenBlock)
				--y;
		}
		captureEntityItems(world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB((double) (chunkX * 16), (double) y, (double) (chunkZ * 16), (double) (chunkX * 16 + 16), (double) pos.getY(), (double) (chunkZ * 16 + 16))));
		tryEject();
	}
	
	public void breakBlock(BlockPos pos, IBlockState state)
	{
		world.setBlockToAir(pos);
		if(state != null)
			HCNetwork.manager.sendToAllAround(new PacketBlock(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, state), getSyncPoint(128));
	}
	
	public NonNullList<ItemStack> makeDrops(BlockPos pos, IBlockState state)
	{
		NonNullList<ItemStack> drops = NonNullList.create();
		state.getBlock().getDrops(drops, world, pos, state, 0);
		return drops;
	}
	
	public boolean canBreak(IBlockState state, BlockPos pos)
	{
		Block b = state.getBlock();
		if(b.getBlockHardness(state, world, pos) < 0F)
			return false;
		if(SimpleQuarry.QUARRY_BLACKLIST.contains(state))
			return false;
		return true;
	}
	
	public void dropStack(ItemStack stack)
	{
		Random rand = world.rand;
		if(!stack.isEmpty() && !world.isRemote)
		{
			EntityItem ei = new EntityItem(world, (double) pos.getX() + 0.5, (double) (pos.getY() + 1), (double) pos.getZ() + 0.5, stack.copy());
			ei.motionX = (rand.nextDouble() - rand.nextDouble()) * 0.045;
			ei.motionY = 1.0 + rand.nextDouble() * 0.5;
			ei.motionZ = (rand.nextDouble() - rand.nextDouble()) * 0.045;
			world.spawnEntity((Entity) ei);
		}
	}
	
	public void tryEject()
	{
		if(!queueItems.isEmpty())
		{
			block0: for(int i = 0; i < queueItems.size(); ++i)
			{
				if(((ItemStack) queueItems.get(i)).isEmpty())
				{
					queueItems.remove(i);
					i = 0;
					continue;
				}
				ItemStack stack = (ItemStack) queueItems.get(i);
				boolean facePut = false;
				for(EnumFacing face : EnumFacing.VALUES)
				{
					int slot;
					IInventory inv;
					TileEntity tile;
					if(face == EnumFacing.DOWN || (inv = (IInventory) WorldUtil.cast((Object) (tile = world.getTileEntity(pos.offset(face))), IInventory.class)) == null || (slot = InvUts.queryItemOrEmptySlot(inv, stack, true, face.getOpposite())) == -1)
						continue;
					InvUts.putItem(inv, slot, stack);
					facePut = true;
					if(!stack.isEmpty())
						continue;
					queueItems.remove(i);
					i = 0;
					continue block0;
				}
				if(facePut || stack.isEmpty())
					continue;
				dropStack(stack);
				queueItems.remove(i);
			}
		}
	}
	
	public void addQueueItem(ItemStack e)
	{
		queueItems.add(e);
	}
	
	public void captureItems(List<ItemStack> items)
	{
		while(!items.isEmpty())
			addQueueItem(items.remove(0));
	}
	
	public void captureEntityItems(List<EntityItem> items)
	{
		for(int j = 0; j < Math.min(items.size(), 1); ++j)
		{
			EntityItem item = items.get(j);
			if(item.getItem().getCount() <= 0)
				continue;
			addQueueItem(item.getItem().copy());
			item.setItem(ItemStack.EMPTY);
			item.setDead();
		}
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		storage.readFromNBT(nbt);
		burnTicks = nbt.getInteger("BurnTicks");
		totalBurnTicks = nbt.getInteger("TotalBurnTicks");
		y = nbt.getInteger("MineY");
		inv.readFromNBT(nbt.getCompoundTag("Items"));
		queueItems.clear();
		int size = nbt.getCompoundTag("QueueItems").getTagList("Items", 10).tagCount();
		for(int i = 0; i < size; ++i)
			queueItems.add(ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt.getCompoundTag("QueueItems"), queueItems);
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		storage.writeToNBT(nbt);
		nbt.setInteger("BurnTicks", burnTicks);
		nbt.setInteger("MineY", y);
		nbt.setInteger("TotalBurnTicks", totalBurnTicks);
		nbt.setTag("Items", inv.writeToNBT(new NBTTagCompound()));
		nbt.setTag("QueueItems", ItemStackHelper.saveAllItems(new NBTTagCompound(), queueItems));
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiFuelQuarry(player, this);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerFuelQuarry(player, this);
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
	
	@Override
	public boolean onWrenchUsed(WorldLocation loc, EntityPlayer player, EnumHand hand)
	{
		if(player.isSneaking())
		{
			WorldUtil.spawnItemStack((WorldLocation) loc, (ItemStack) new ItemStack(loc.getBlock(), 1));
			world.setBlockToAir(pos);
			return true;
		}
		return false;
	}
	
	@Override
	public void createDrop(EntityPlayer player, World world, BlockPos pos)
	{
		inv.drop(world, pos);
		for(ItemStack stack : queueItems)
			dropStack(stack);
		queueItems.clear();
		if(this instanceof TilePoweredQuarry)
			((TilePoweredQuarry) this).invUpgrades.drop(world, pos);
	}
}