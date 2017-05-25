package com.mrdimka.simplequarry.init;

import com.mrdimka.simplequarry.cfg.ModConfig;
import com.mrdimka.simplequarry.utils.ArrayHashSet;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipesSQ
{
	public static ArrayHashSet<ShapedOreRecipe> recipesReg = new ArrayHashSet<ShapedOreRecipe>();
	public static void registerRecipe(ItemStack result, Object... recipe)
	{
		ShapedOreRecipe sor = new ShapedOreRecipe(result, recipe);
		GameRegistry.addRecipe(sor);
		recipesReg.add(sor);
	}
	
	public static void reload()
	{
		for(Object o : recipesReg.toArray()) CraftingManager.getInstance().getRecipeList().remove(o);
		registerRecipe(new ItemStack(BlocksSQ.fuel_quarry), "pip", "fgf", "pdp", 'p', Items.ENDER_PEARL, 'i', Items.IRON_PICKAXE, 'f', Blocks.FURNACE, 'g', Items.GOLDEN_PICKAXE, 'd', Items.DIAMOND_PICKAXE);
		if(ModConfig.POWERED_QUARRY_RECIPE == 0) registerRecipe(new ItemStack(BlocksSQ.powered_quarry), "ehe", "dqd", "ece", 'e', Items.ENDER_EYE, 'h', Blocks.HOPPER, 'c', "chestWood", 'd', Items.DIAMOND_PICKAXE, 'q', BlocksSQ.fuel_quarry);
		else if(ModConfig.POWERED_QUARRY_RECIPE == 1) registerRecipe(new ItemStack(BlocksSQ.powered_quarry), "phl", "dqd", "sem", 'p', new ItemStack(Blocks.DIRT, 1, 2), 'h', Blocks.HOPPER, 'l', Blocks.SEA_LANTERN, 'd', Items.DIAMOND_PICKAXE, 'q', BlocksSQ.fuel_quarry, 's', Blocks.SLIME_BLOCK, 'e', Blocks.ENDER_CHEST, 'm', Blocks.MAGMA);
	}
}