package com.endie.simplequarry.items;

import com.endie.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class ItemFillerUpgrade extends ItemUpgrade
{
	{
		setUnlocalizedName("upgrade_filler");
		quarryUseMultiply = 1.5F;
	}
	
	@Override
	public void handleDrops(TilePoweredQuarry quarry, BlockPos pos, NonNullList<ItemStack> drops)
	{
		NBTTagList list = quarry.additionalTags.getTagList("RestorePositions", NBT.TAG_LONG);
		if(list.hasNoTags())
			quarry.additionalTags.setTag("RestorePositions", list);
		list.appendTag(new NBTTagLong(pos.toLong()));
		
		for(int i = 0; i < drops.size(); ++i)
			if(drops.get(i).getItem() == Item.getItemFromBlock(Blocks.DIRT))
				drops.remove(i);
	}
	
	@Override
	public void tick(TilePoweredQuarry quarry, int index)
	{
		if(quarry.isDone())
		{
			NBTTagList list = quarry.additionalTags.getTagList("RestorePositions", NBT.TAG_LONG);
			
			if(quarry.atTickRate(5) && !list.hasNoTags())
			{
				NBTTagLong pos = (NBTTagLong) list.removeTag(list.tagCount() - 1);
				BlockPos bp = BlockPos.fromLong(pos.getLong());
				World w = quarry.getWorld();
				if(!w.isRemote && w.isAirBlock(bp))
					w.setBlockState(bp, Blocks.DIRT.getStateFromMeta(1));
			} else if(list.hasNoTags())
				quarry.additionalTags.removeTag("RestorePositions");
		}
	}
	
	@Override
	public boolean isCompatible(TilePoweredQuarry quarry)
	{
		return !hasUpgrade(quarry, this);
	}
	
	@Override
	public boolean canStay(TilePoweredQuarry quarry, int index)
	{
		return true;
	}
}