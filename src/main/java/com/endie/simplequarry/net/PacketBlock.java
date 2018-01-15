package com.endie.simplequarry.net;

import com.endie.simplequarry.client.particle.ParticleBlock;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.hammercore.proxy.ParticleProxy_Client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketBlock implements iPacket, iPacketListener<PacketBlock, iPacket>
{
	double x, y, z;
	IBlockState state;
	
	public PacketBlock()
	{
	}
	
	public PacketBlock(double x, double y, double z, IBlockState state)
	{
		this.state = state;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public iPacket onArrived(PacketBlock packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		ParticleProxy_Client.queueParticleSpawn(new ParticleBlock(Minecraft.getMinecraft().world, x, y, z, state));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		if(state != null)
			nbt.setInteger("State", Block.getStateId(state));
		nbt.setDouble("x", x);
		nbt.setDouble("y", y);
		nbt.setDouble("z", z);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("State"))
			state = Block.getStateById(nbt.getInteger("State"));
		x = nbt.getDouble("x");
		y = nbt.getDouble("y");
		z = nbt.getDouble("z");
	}
}