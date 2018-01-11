package com.endie.simplequarry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import com.endie.simplequarry.cfg.ConfigsSQ;
import com.endie.simplequarry.init.BlocksSQ;
import com.endie.simplequarry.init.GuisSQ;
import com.endie.simplequarry.init.ItemsSQ;
import com.endie.simplequarry.init.RecipesSQ;
import com.endie.simplequarry.proxy.CommonProxy;
import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.hammercore.common.utils.HammerCoreUtils;
import com.pengu.hammercore.common.utils.WrappedLog;
import com.pengu.hammercore.intent.IntentManager;
import com.pengu.hammercore.var.VariableManager;
import com.pengu.hammercore.var.iVariable;
import com.pengu.hammercore.var.types.VariableString;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = InfoSQ.MOD_ID, name = InfoSQ.MOD_NAME, version = InfoSQ.MOD_VERSION, dependencies = "required-after:hammercore", guiFactory = "com.endie.simplequarry.cfg.ConfigFactorySQ", certificateFingerprint = "4d7b29cd19124e986da685107d16ce4b49bc0a97")
public class SimpleQuarry
{
	@SidedProxy(modId = "simplequarry", clientSide = "com.endie.simplequarry.proxy.ClientProxy", serverSide = "com.endie.simplequarry.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Mod.Instance(value = "simplequarry")
	public static SimpleQuarry instance;
	
	public static boolean invalidCertificate;
	public static final iVariable<String> SYNC_CONFIGS;
	public static NBTTagCompound sync_configs_nbt;
	public static CreativeTabs tab;
	public static final Set<IBlockState> QUARRY_BLACKLIST;
	public static WrappedLog LOG = new WrappedLog(InfoSQ.MOD_NAME);
	
	@EventHandler
	public void certificateViolation(FMLFingerprintViolationEvent e)
	{
		LOG.warn("*****************************");
		LOG.warn("WARNING: Somebody has been tampering with SimpleQuarry jar!");
		LOG.warn("It is highly recommended that you redownload mod from https://minecraft.curseforge.com/projects/247393 !");
		LOG.warn("*****************************");
		invalidCertificate = true;
	}
	
	@EventHandler
	public void construct(FMLConstructionEvent e)
	{
		IntentManager.registerIntentHandler("simplequarry:quarry_blacklist", Supplier.class, (mod, data) -> QUARRY_BLACKLIST.add((IBlockState) data.get()));
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		VariableManager.registerVariable(SYNC_CONFIGS);
		
		SYNC_CONFIGS.set("");
		
		tab = HammerCoreUtils.createDynamicCreativeTab("simplequarry", 80);
		
		MinecraftForge.EVENT_BUS.register(proxy);
		MinecraftForge.EVENT_BUS.register(this);
		
		proxy.preInit();
		
		SimpleRegistration.registerFieldBlocksFrom(BlocksSQ.class, "simplequarry", tab);
		SimpleRegistration.registerFieldItemsFrom(ItemsSQ.class, "simplequarry", tab);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.init();
		new GuisSQ();
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
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("BPC", ConfigsSQ.BLOCKS_PER_COAL);
		nbt.setInteger("QuarryRecipe", ConfigsSQ.POWERED_QUARRY_RECIPE);
		SYNC_CONFIGS.set(nbt.toString());
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
	public void addRecipes(RegistryEvent.Register<IRecipe> reg)
	{
		IForgeRegistry<IRecipe> fr = reg.getRegistry();
		RecipesSQ.collect() //
		        .stream() //
		        .filter(r -> r != null) //
		        .forEach(fr::register);
	}
	
	static
	{
		SYNC_CONFIGS = new VariableString("simplequarry:configs");
		sync_configs_nbt = new NBTTagCompound();
		QUARRY_BLACKLIST = new HashSet<IBlockState>();
	}
}
