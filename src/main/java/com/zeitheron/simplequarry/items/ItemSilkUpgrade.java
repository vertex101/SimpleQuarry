package com.zeitheron.simplequarry.items;

import com.zeitheron.simplequarry.init.ItemsSQ;
import com.zeitheron.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSilkUpgrade extends ItemUpgrade
{
	{
		setUnlocalizedName("upgrade_silk");
		quarryUseMultiply = 8;
	}
	
	@Override
	public void handleDrops(TilePoweredQuarry quarry, BlockPos pos, NonNullList<ItemStack> drops)
	{
		World world = quarry.getWorld();
		IBlockState state = world.getBlockState(pos);
		
		boolean silkable = false;
		
		try
		{
			silkable = state.getBlock().canSilkHarvest(world, pos, state, null);
		} catch(NullPointerException npe)
		{
			// Bc player is null, some mods may throw NPE
		}
		
		if(silkable)
		{
			drops.clear();
			drops.add(state.getBlock().getItem(world, pos, state));
		}
	}
	
	@Override
	public boolean isCompatible(TilePoweredQuarry quarry)
	{
		return !hasUpgrade(quarry, this) && quarry.getFortune() == 0 && !hasUpgrade(quarry, ItemsSQ.UPGRADE_AUTO_SMELT);
	}
	
	@Override
	public boolean canStay(TilePoweredQuarry quarry, int index)
	{
		return quarry.getFortune() == 0 && !hasUpgrade(quarry, ItemsSQ.UPGRADE_AUTO_SMELT);
	}
}