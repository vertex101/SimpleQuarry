package com.zeitheron.simplequarry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.intent.IntentManager;
import com.zeitheron.hammercore.internal.SimpleRegistration;
import com.zeitheron.hammercore.internal.variables.IVariable;
import com.zeitheron.hammercore.internal.variables.VariableManager;
import com.zeitheron.hammercore.internal.variables.types.VariableString;
import com.zeitheron.hammercore.utils.HammerCoreUtils;
import com.zeitheron.hammercore.utils.WrappedLog;
import com.zeitheron.simplequarry.cfg.ConfigsSQ;
import com.zeitheron.simplequarry.init.BlocksSQ;
import com.zeitheron.simplequarry.init.GuisSQ;
import com.zeitheron.simplequarry.init.ItemsSQ;
import com.zeitheron.simplequarry.proxy.CommonProxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
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

@Mod(modid = InfoSQ.MOD_ID, name = InfoSQ.MOD_NAME, version = InfoSQ.MOD_VERSION, dependencies = "required-after:hammercore", guiFactory = "com.zeitheron.simplequarry.cfg.ConfigFactorySQ", certificateFingerprint = "4d7b29cd19124e986da685107d16ce4b49bc0a97")
public class SimpleQuarry
{
	@SidedProxy(modId = InfoSQ.MOD_ID, clientSide = "com.zeitheron.simplequarry.proxy.ClientProxy", serverSide = "com.zeitheron.simplequarry.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Mod.Instance(InfoSQ.MOD_ID)
	public static SimpleQuarry instance;
	
	public static boolean invalidCertificate;
	public static final IVariable<String> SYNC_CONFIGS;
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
		HammerCore.invalidCertificates.put(InfoSQ.MOD_ID, "https://minecraft.curseforge.com/projects/247393");
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
	
	static
	{
		SYNC_CONFIGS = new VariableString("simplequarry:configs");
		sync_configs_nbt = new NBTTagCompound();
		QUARRY_BLACKLIST = new HashSet<IBlockState>();
	}
}
