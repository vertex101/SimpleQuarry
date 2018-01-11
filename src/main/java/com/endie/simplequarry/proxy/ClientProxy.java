package com.endie.simplequarry.proxy;

import java.util.HashSet;
import java.util.Set;

import com.endie.simplequarry.SimpleQuarry;
import com.endie.simplequarry.api.energy.UniversalConverter;
import com.endie.simplequarry.cfg.ConfigsSQ;
import com.endie.simplequarry.gui.c.ContainerFuelQuarry;
import com.endie.simplequarry.gui.c.ContainerPoweredQuarry;
import com.endie.simplequarry.items.ItemUpgrade;
import com.endie.simplequarry.tile.TileFuelQuarry;
import com.endie.simplequarry.vortex.Vortex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientProxy extends CommonProxy
{
	private static String $str_sync_configs = "";
	public static Set<Vortex> particleVortex = new HashSet<Vortex>();
	
	@Override
	public void addParticleVortex(Vortex vortex)
	{
		if(vortex == null || vortex.getVortexStrenght() == 0.0 || particleVortex.contains(vortex))
			return;
		HashSet<Vortex> particleVortex = new HashSet<Vortex>(ClientProxy.particleVortex);
		particleVortex.add(vortex);
		ClientProxy.particleVortex = particleVortex;
	}
	
	@Override
	public void removeParticleVortex(Vortex vortex)
	{
		if(vortex == null || vortex.getVortexStrenght() == 0.0 || !particleVortex.contains(vortex))
			return;
		HashSet<Vortex> particleVortex = new HashSet<Vortex>(ClientProxy.particleVortex);
		particleVortex.remove(vortex);
		ClientProxy.particleVortex = particleVortex;
	}
	
	@SubscribeEvent
	public void tickParticle(TickEvent.ClientTickEvent evt)
	{
		for(Vortex vortex : particleVortex)
			vortex.update();
		String data = SimpleQuarry.SYNC_CONFIGS.get();
		if(data != null && data != $str_sync_configs)
		{
			$str_sync_configs = data;
			try
			{
				NBTTagCompound nbt = SimpleQuarry.sync_configs_nbt = JsonToNBT.getTagFromJson((String) data);
				ConfigsSQ.BLOCKS_PER_COAL = nbt.getFloat("BPC");
				ConfigsSQ.POWERED_QUARRY_RECIPE = nbt.getInteger("QuarryRecipe");
			} catch(NBTException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void sendPacket(Packet<?> pkt)
	{
		Minecraft.getMinecraft().player.connection.sendPacket(pkt);
	}
	
	@SubscribeEvent
	public void tooltip(ItemTooltipEvent e)
	{
		EntityPlayer p = e.getEntityPlayer();
		if(p == null)
			return;
		
		ItemStack it = e.getItemStack();
		
		if(!it.isEmpty() && it.getItem() instanceof ItemUpgrade)
		{
			ItemUpgrade up = (ItemUpgrade) it.getItem();
			
			e.getToolTip().add(TextFormatting.DARK_PURPLE + I18n.format("info.simplequarry:fuel_use_boost") + ": x" + up.quarryUseMultiply);
		}
		
		if(p.openContainer instanceof ContainerFuelQuarry || p.openContainer instanceof ContainerPoweredQuarry)
		{
			TileFuelQuarry quarry = null;
			
			if(p.openContainer instanceof ContainerFuelQuarry)
				quarry = ((ContainerFuelQuarry) p.openContainer).t;
			
			if(p.openContainer instanceof ContainerPoweredQuarry)
				quarry = ((ContainerPoweredQuarry) p.openContainer).t;
			
			int burnTime = TileEntityFurnace.getItemBurnTime(e.getItemStack());
			
			if(burnTime > 0)
			{
				float mod = quarry != null ? quarry.getUsageMult() : 1F;
				e.getToolTip().add(TextFormatting.DARK_GRAY + I18n.format("info.simplequarry:blocks_broken") + ": " + (int) (UniversalConverter.FT_QF(burnTime / mod) / (UniversalConverter.FT_QF(TileEntityFurnace.getItemBurnTime(COAL)) / ConfigsSQ.BLOCKS_PER_COAL)));
				e.getToolTip().add(TextFormatting.DARK_GRAY + I18n.format("info.simplequarry:fuel_use_boost") + ": " + (int) (mod * 100) + "%");
			}
			else
				e.getToolTip().add(TextFormatting.DARK_GRAY + I18n.format("info.simplequarry:not_fuel"));
		}
	}
}
