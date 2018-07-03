package com.zeitheron.simplequarry.items;

import com.zeitheron.hammercore.internal.GuiManager;
import com.zeitheron.simplequarry.init.GuisSQ;
import com.zeitheron.simplequarry.init.ItemsSQ;
import com.zeitheron.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUnificationUpgrade extends ItemUpgrade
{
	public ItemUnificationUpgrade()
	{
		setUnlocalizedName("upgrade_unification");
		quarryUseMultiply = 1.25F;
	}
	
	public static boolean isUnificationUpgrade(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getItem() == ItemsSQ.UPGRADE_UNIFICATION;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(handIn == EnumHand.MAIN_HAND)
			GuiManager.openGuiCallback(GuisSQ.UNIFICATION, playerIn, worldIn, playerIn.getPosition());
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public ItemStack handlePickup(ItemStack stack, TilePoweredQuarry quarry, int index)
	{
		ItemStack filter = ItemStack.EMPTY;
		ItemStack unifi = quarry.getUpgradeStack(index);
		if(unifi.hasTagCompound())
			filter = new ItemStack(unifi.getTagCompound().getCompoundTag("Filter"));
		
		if(!filter.isEmpty() && !ItemFilterUpgrade.matches(filter, stack))
			return stack;
		
		int[] ids = OreDictionary.getOreIDs(stack);
		if(ids.length > 0)
			for(int id : ids)
			{
				NonNullList<ItemStack> stacksod = OreDictionary.getOres(OreDictionary.getOreName(id));
				
				if(!stacksod.isEmpty())
				{
					ItemStack ns = stacksod.get(0).copy();
					ns.setCount(stack.getCount() * ns.getCount());
					if(ns.getItemDamage() == OreDictionary.WILDCARD_VALUE)
						ns.setItemDamage(0);
					return ns;
				}
			}
		
		return stack;
	}
	
	@Override
	public boolean isCompatible(TilePoweredQuarry quarry)
	{
		return !hasUpgrade(quarry, this);
	}
}