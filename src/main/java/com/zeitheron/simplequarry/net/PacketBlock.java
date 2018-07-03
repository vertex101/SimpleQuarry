package com.zeitheron.simplequarry.net;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.proxy.ParticleProxy_Client;
import com.zeitheron.simplequarry.client.particle.ParticleBlock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketBlock implements IPacket
{
	double x, y, z;
	IBlockState state;
	
	static
	{
		IPacket.handle(PacketBlock.class, PacketBlock::new);
	}
	
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
	@SideOnly(Side.CLIENT)
	public IPacket executeOnClient(PacketContext net)
	{
		ParticleProxy_Client.queueParticleSpawn(new ParticleBlock(Minecraft.getMinecraft().world, x, y, z, state));
		return null;
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