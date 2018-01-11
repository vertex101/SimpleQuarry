package com.endie.simplequarry.proxy;

import com.endie.simplequarry.vortex.Vortex;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;

public class CommonProxy
{
	public static final ItemStack COAL = new ItemStack(Items.COAL);
	
	public void preInit()
	{
	}
	
	public void init()
	{
	}
	
	public void postInit()
	{
	}
	
	public void serverStarting()
	{
	}
	
	public void serverStarted()
	{
	}
	
	public void serverStopping()
	{
	}
	
	public void serverStopped()
	{
	}
	
	public void sendPacket(Packet<?> pkt)
	{
	}
	
	public void addParticleVortex(Vortex vortex)
	{
	}
	
	public void removeParticleVortex(Vortex vortex)
	{
	}
}