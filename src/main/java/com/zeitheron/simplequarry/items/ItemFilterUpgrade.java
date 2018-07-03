package com.zeitheron.simplequarry.items;

import java.util.Objects;

import com.zeitheron.hammercore.internal.GuiManager;
import com.zeitheron.hammercore.utils.InterItemStack;
import com.zeitheron.simplequarry.init.GuisSQ;
import com.zeitheron.simplequarry.init.ItemsSQ;
import com.zeitheron.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.oredict.OreDictionary;

public class ItemFilterUpgrade extends ItemUpgrade
{
	public ItemFilterUpgrade()
	{
		setUnlocalizedName("upgrade_filter");
		quarryUseMultiply = 1;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(handIn == EnumHand.MAIN_HAND)
			GuiManager.openGuiCallback(GuisSQ.FILTER, playerIn, worldIn, playerIn.getPosition());
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	public static boolean matches(ItemStack filter, ItemStack input)
	{
		NBTTagCompound nbt = filter.getTagCompound();
		
		if(nbt != null)
		{
			boolean invert = nbt.getBoolean("InvertList");
			boolean useod = nbt.hasKey("OreDictionary") ? nbt.getBoolean("OreDictionary") : false;
			boolean usemeta = nbt.hasKey("Metadata") ? nbt.getBoolean("Metadata") : true;
			boolean ignorenbt = nbt.hasKey("IgnoreNBT") ? nbt.getBoolean("IgnoreNBT") : true;
			
			boolean applies = false;
			
			NBTTagList list = nbt.getTagList("Filter", NBT.TAG_COMPOUND);
			
			for(int i = 0; i < list.tagCount(); ++i)
			{
				ItemStack ft = new ItemStack(list.getCompoundTagAt(i));
				if(ft.isEmpty())
					continue;
				
				if(ft.getItem() == ItemsSQ.UPGRADE_FILTER && matches(ft, input))
				{
					applies = true;
					break;
				} else
				{
					boolean m1 = useod ? matchesByOD(input, ft) : false;
					
					boolean m0 = ft.getItem() == input.getItem();
					boolean m2 = !usemeta ? true : ft.getItemDamage() == input.getItemDamage();
					boolean m3 = ignorenbt ? true : Objects.equals(ft.getTagCompound(), input.getTagCompound());
					
					if(m1 || (m0 && m2 && m3))
					{
						applies = true;
						break;
					}
				}
			}
			
			return invert ? applies : !applies;
		}
		
		return true;
	}
	
	public static boolean isFilterUpgrade(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getItem() == ItemsSQ.UPGRADE_FILTER;
	}
	
	public static boolean matchesByOD(ItemStack a, ItemStack b)
	{
		if(a.isEmpty() && b.isEmpty())
			return true;
		
		if(a.isEmpty() || b.isEmpty())
			return false;
		
		int[] ai = OreDictionary.getOreIDs(a);
		int[] bi = OreDictionary.getOreIDs(b);
		
		for(int i : ai)
			for(int j : bi)
				if(OreDictionary.getOreName(i).equalsIgnoreCase(OreDictionary.getOreName(j)))
					return true;
				
		return false;
	}
	
	@Override
	public ItemStack handlePickup(ItemStack stack, TilePoweredQuarry quarry, int index)
	{
		if(!matches(quarry.getUpgradeStack(index), stack))
			return InterItemStack.NULL_STACK;
		return stack;
	}
	
	@Override
	public boolean isCompatible(TilePoweredQuarry quarry)
	{
		return !hasUpgrade(quarry, this);
	}
}