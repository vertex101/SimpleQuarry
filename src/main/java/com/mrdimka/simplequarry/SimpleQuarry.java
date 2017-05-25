package com.mrdimka.simplequarry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mrdimka.hammercore.common.utils.HammerCoreUtils;
import com.mrdimka.hammercore.init.SimpleRegistration;
import com.mrdimka.simplequarry.api.energy.UniversalConverter;
import com.mrdimka.simplequarry.gui.c.ContainerFuelQuarry;
import com.mrdimka.simplequarry.gui.c.ContainerPoweredQuarry;
import com.mrdimka.simplequarry.init.BlocksSQ;
import com.mrdimka.simplequarry.init.ItemsSQ;
import com.mrdimka.simplequarry.init.RecipesSQ;
import com.mrdimka.simplequarry.proxy.CommonProxy;
import com.mrdimka.simplequarry.ref.ModInfo;
import com.mrdimka.simplequarry.tile.TileFuelQuarry;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = "required-after:hammercore")
public class SimpleQuarry
{
	@SidedProxy(modId = ModInfo.MOD_ID, clientSide = "com.mrdimka.simplequarry.proxy.ClientProxy", serverSide = "com.mrdimka.simplequarry.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance(ModInfo.MOD_ID)
	public static SimpleQuarry instance;
	
	public static CreativeTabs tab;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		tab = HammerCoreUtils.createDynamicCreativeTab(ModInfo.MOD_ID, 80);
		proxy.preInit();
		
		SimpleRegistration.registerFieldBlocksFrom(BlocksSQ.class, ModInfo.MOD_ID, tab);
		SimpleRegistration.registerFieldItemsFrom(ItemsSQ.class, ModInfo.MOD_ID, tab);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.init();
		MinecraftForge.EVENT_BUS.register(this);
		RecipesSQ.reload();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		proxy.postInit();
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent e)
	{
		proxy.serverStarting();
	}
	
	@EventHandler
	public void serverStarted(FMLServerStartedEvent e)
	{
		proxy.serverStarted();
	}
	
	@EventHandler
	public void serverStopping(FMLServerStoppingEvent e)
	{
		proxy.serverStopping();
	}
	
	@EventHandler
	public void serverStopped(FMLServerStoppedEvent e)
	{
		proxy.serverStopped();
	}
	
	@SubscribeEvent
	public void tooltip(ItemTooltipEvent e)
	{
		EntityPlayer p = e.getEntityPlayer();
		if(p == null) return; //Fix the spam log of ItemStack prep
		if(p.openContainer instanceof ContainerFuelQuarry || p.openContainer instanceof ContainerPoweredQuarry)
		{
			int burnTime = TileEntityFurnace.getItemBurnTime(e.getItemStack());
			if(burnTime > 0) e.getToolTip().add("\u00A78" + I18n.translateToLocal("info." + ModInfo.MOD_ID + ":blocks_broken") + ": " + (int) (UniversalConverter.FT_QF(burnTime) / (UniversalConverter.FT_QF(1600) / TileFuelQuarry.BPC)));
			else e.getToolTip().add("\u00A78" + I18n.translateToLocal("info." + ModInfo.MOD_ID + ":not_fuel"));
		}
	}
}