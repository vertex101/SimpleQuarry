package com.endie.simplequarry.init;

import com.endie.simplequarry.InfoSQ;
import com.endie.simplequarry.cfg.ConfigsSQ;
import com.pengu.hammercore.core.init.ItemsHC;
import com.pengu.hammercore.recipeAPI.helper.RecipeRegistry;
import com.pengu.hammercore.recipeAPI.helper.RegisterRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

@RegisterRecipes(modid = InfoSQ.MOD_ID)
public class RecipesSQ extends RecipeRegistry
{
	@Override
	public void smelting()
	{
		
	}
	
	@Override
	public void crafting()
	{
		shaped(new ItemStack(BlocksSQ.fuel_quarry), "pip", "fgf", "pdp", 'p', "enderpearl", 'i', Items.IRON_PICKAXE, 'f', Blocks.FURNACE, 'g', "gearIron", 'd', Items.DIAMOND_PICKAXE);
		
		shaped(new ItemStack(ItemsSQ.UPGRADE_BASE), "rir", "igi", "rir", 'r', "dustRedstone", 'i', "ingotIron", 'g', "gearIron");
		shaped(new ItemStack(ItemsSQ.UPGRADE_FILTER), "rsr", "sus", "rsr", 'r', "dustRedstone", 's', "string", 'u', ItemsSQ.UPGRADE_BASE);
		shaped(ItemsSQ.UPGRADE_FORTUNE1, "ibi", "dud", "idi", 'i', "ingotIron", 'b', enchantedBook(Enchantments.FORTUNE, 1), 'd', "gemDiamond", 'u', ItemsSQ.UPGRADE_BASE);
		shaped(ItemsSQ.UPGRADE_FORTUNE2, "ibi", "dud", "idi", 'i', "ingotGold", 'b', enchantedBook(Enchantments.FORTUNE, 2), 'd', "gemEmerald", 'u', ItemsSQ.UPGRADE_BASE);
		shaped(ItemsSQ.UPGRADE_FORTUNE3, "ibi", "dud", "idi", 'i', new ItemStack(Blocks.PURPUR_BLOCK, 1, OreDictionary.WILDCARD_VALUE), 'b', enchantedBook(Enchantments.FORTUNE, 3), 'd', "netherStar", 'u', ItemsSQ.UPGRADE_BASE);
		shaped(ItemsSQ.UPGRADE_SILK, "lbl", "puh", "lel", 'l', Blocks.SEA_LANTERN, 'b', enchantedBook(Enchantments.SILK_TOUCH, 1), 'p', Items.GOLDEN_PICKAXE, 'u', ItemsSQ.UPGRADE_BASE, 'h', Items.GOLDEN_SHOVEL, 'e', "gemEmerald");
		shaped(ItemsSQ.UPGRADE_UNIFICATION, "beb", "gug", "bgb", 'g', Blocks.BOOKSHELF, 'e', Blocks.ENCHANTING_TABLE, 'b', "ingotGold", 'u', ItemsSQ.UPGRADE_BASE);
		shaped(ItemsSQ.UPGRADE_AUTO_SMELT, "olo", "lul", "olo", 'o', "obsidian", 'l', Items.LAVA_BUCKET, 'u', ItemsSQ.UPGRADE_BASE);
		shaped(ItemsSQ.UPGRADE_EFFICIENCY1, "iri", "rur", "iri", 'i', "ingotIron", 'r', "blockRedstone", 'u', ItemsSQ.UPGRADE_BASE);
		shaped(ItemsSQ.UPGRADE_EFFICIENCY2, "ibi", "rur", "ibi", 'i', "ingotGold", 'r', "blockRedstone", 'u', ItemsSQ.UPGRADE_BASE, 'b', ItemsHC.BATTERY);
		shaped(ItemsSQ.UPGRADE_EFFICIENCY3, "ibi", "bub", "ibi", 'i', "gemDiamond", 'u', ItemsSQ.UPGRADE_BASE, 'b', ItemsHC.BATTERY);
		shaped(ItemsSQ.UPGRADE_FILLER, "mdm", "dud", "mdm", 'm', Items.DIAMOND_SHOVEL, 'd', Blocks.GRASS, 'u', ItemsSQ.UPGRADE_BASE);
		
		if(ConfigsSQ.POWERED_QUARRY_RECIPE == 0)
			shaped(new ItemStack(BlocksSQ.powered_quarry), "ehe", "dqd", "ece", 'c', "chestWood", 'q', BlocksSQ.fuel_quarry, 'h', Blocks.HOPPER, 'e', Items.ENDER_EYE, 'd', Items.DIAMOND_PICKAXE);
		if(ConfigsSQ.POWERED_QUARRY_RECIPE == 1)
			shaped(new ItemStack(BlocksSQ.powered_quarry), "phl", "dqd", "sem", 'q', BlocksSQ.fuel_quarry, 'd', Items.DIAMOND_PICKAXE, 'p', new ItemStack(Blocks.DIRT, 1, 2), 'h', Blocks.HOPPER, 'l', Blocks.SEA_LANTERN, 's', Blocks.SLIME_BLOCK, 'e', Blocks.ENDER_CHEST, 'm', Blocks.MAGMA);
	}
	
	@Override
	protected String getMod()
	{
		return InfoSQ.MOD_ID;
	}
	
	@Override
	protected void recipe(IRecipe recipe)
	{
		if(recipe.getRegistryName() == null)
			recipe = recipe.setRegistryName(new ResourceLocation("hammercore", "recipes." + getMod() + "_" + getClass().getSimpleName() + "." + recipes.size()));
		recipes.add(recipe);
	}
}