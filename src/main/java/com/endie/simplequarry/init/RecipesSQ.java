package com.endie.simplequarry.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.endie.simplequarry.InfoSQ;
import com.endie.simplequarry.cfg.ConfigsSQ;
import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.hammercore.core.init.ItemsHC;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

public class RecipesSQ
{
	public static void furnace()
	{
		
	}
	
	private static void craftingTable()
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
		shaped(ItemsSQ.UPGRADE_EFFICIENCY2, "ibi", "rur", "ibi", 'i', "ingotGold", 'r', "blockRedstone", 'u', ItemsSQ.UPGRADE_BASE, 'b', ItemsHC.battery);
		shaped(ItemsSQ.UPGRADE_EFFICIENCY3, "ibi", "bub", "ibi", 'i', "gemDiamond", 'u', ItemsSQ.UPGRADE_BASE, 'b', ItemsHC.battery);
		
		if(ConfigsSQ.POWERED_QUARRY_RECIPE == 0)
			shaped(new ItemStack(BlocksSQ.powered_quarry), "ehe", "dqd", "ece", 'c', "chestWood", 'q', BlocksSQ.fuel_quarry, 'h', Blocks.HOPPER, 'e', Items.ENDER_EYE, 'd', Items.DIAMOND_PICKAXE);
		if(ConfigsSQ.POWERED_QUARRY_RECIPE == 1)
			shaped(new ItemStack(BlocksSQ.powered_quarry), "phl", "dqd", "sem", 'q', BlocksSQ.fuel_quarry, 'd', Items.DIAMOND_PICKAXE, 'p', new ItemStack(Blocks.DIRT, 1, 2), 'h', Blocks.HOPPER, 'l', Blocks.SEA_LANTERN, 's', Blocks.SLIME_BLOCK, 'e', Blocks.ENDER_CHEST, 'm', Blocks.MAGMA);
	}
	
	public static ItemStack enchantedBook(Enchantment ench, int lvl)
	{
		ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
		Map<Enchantment, Integer> enchs = new HashMap<>();
		enchs.put(ench, lvl);
		EnchantmentHelper.setEnchantments(enchs, book);
		return book;
	}
	
	private static final List<IRecipe> recipes = new ArrayList<>();
	
	public static Collection<IRecipe> collect()
	{
		craftingTable();
		HashSet<IRecipe> recipes = new HashSet<>(RecipesSQ.recipes);
		RecipesSQ.recipes.clear();
		return recipes;
	}
	
	private static void smelting(ItemStack in, ItemStack out)
	{
		smelting(in, out, 0);
	}
	
	private static void smelting(Item in, ItemStack out, float xp)
	{
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(in), out, xp);
	}
	
	private static void smelting(ItemStack in, ItemStack out, float xp)
	{
		FurnaceRecipes.instance().addSmeltingRecipe(in, out, xp);
	}
	
	private static void shaped(ItemStack out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapedRecipe(out, recipeComponents).setRegistryName(InfoSQ.MOD_ID, "recipes." + recipes.size()));
	}
	
	private static void shaped(Item out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapedRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoSQ.MOD_ID, "recipes." + recipes.size()));
	}
	
	private static void shaped(Block out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapedRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoSQ.MOD_ID, "recipes." + recipes.size()));
	}
	
	private static void shapeless(ItemStack out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapelessRecipe(out, recipeComponents).setRegistryName(InfoSQ.MOD_ID, "recipes." + recipes.size()));
	}
	
	private static void shapeless(Item out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapelessRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoSQ.MOD_ID, "recipes." + recipes.size()));
	}
	
	private static void recipe(IRecipe recipe)
	{
		recipes.add(recipe);
	}
	
	private static void shapeless(Block out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapelessRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoSQ.MOD_ID, "recipes." + recipes.size()));
	}
}