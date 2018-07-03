package com.zeitheron.simplequarry.items;

import com.zeitheron.simplequarry.init.ItemsSQ;
import com.zeitheron.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemAutoSmeltUpgrade extends ItemUpgrade
{
	{
		setUnlocalizedName("upgrade_auto_smelt");
		quarryUseMultiply = 4;
	}
	
	@Override
	public void handleDrops(TilePoweredQuarry quarry, BlockPos pos, NonNullList<ItemStack> drops)
	{
		for(int i = 0; i < drops.size(); ++i)
		{
			ItemStack res = FurnaceRecipes.instance().getSmeltingResult(drops.get(i)).copy();
			if(!res.isEmpty())
			{
				res.setCount(res.getCount() * drops.get(i).getCount());
				drops.set(i, res);
			}
		}
	}
	
	@Override
	public boolean isCompatible(TilePoweredQuarry quarry)
	{
		return !hasUpgrade(quarry, this) && !hasUpgrade(quarry, ItemsSQ.UPGRADE_SILK);
	}
	
	@Override
	public boolean canStay(TilePoweredQuarry quarry, int index)
	{
		return !hasUpgrade(quarry, ItemsSQ.UPGRADE_SILK);
	}
}