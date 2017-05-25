package com.mrdimka.simplequarry.proxy;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.FMLLog;

import org.apache.logging.log4j.Level;

import com.mrdimka.simplequarry.init.ItemsSQ;
import com.mrdimka.simplequarry.ref.ModInfo;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
	}
	
	@Override
	public void init()
	{
	}
	
	@Override
	public void postInit()
	{
	}
	
	@Override
	public void serverStarting()
	{
	}
	
	@Override
	public void serverStarted()
	{
		
	}
	
	@Override
	public void serverStopping()
	{
		
	}
	
	@Override
	public void serverStopped()
	{
	}
	
	@Override
	public void sendPacket(Packet<?> pkt)
	{
		Minecraft.getMinecraft().player.connection.sendPacket(pkt);
	}
}