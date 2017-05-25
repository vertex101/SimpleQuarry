package com.mrdimka.simplequarry.utils;

import java.awt.Color;

import com.google.common.base.Optional;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireworkUtils
{
	public static void spawnRandomFirework(World world, double x, double y, double z)
	{
		ItemStack item = new ItemStack(Items.FIREWORKS);
		NBTTagCompound nbt, nbt2;
		item.setTagCompound(nbt = new NBTTagCompound());
		nbt.setTag("Fireworks", nbt2 = new NBTTagCompound());
		nbt2.setInteger("Flight", 1);
		NBTTagList list = new NBTTagList();
		
		for(int o = 0; o < 1; ++o)
		{
			NBTTagCompound nbt0 = new NBTTagCompound();
			
			nbt0.setBoolean("Trail", world.rand.nextBoolean());
			nbt0.setByte("Type", (byte) world.rand.nextInt(8));
			
			NBTTagList list0 = new NBTTagList();
			for(int i = 0; i < 6 + world.rand.nextInt(6); ++i)
			{
				int rgb = new Color(world.rand.nextInt(256), world.rand.nextInt(256), world.rand.nextInt(256)).getRGB();
				list0.appendTag(new NBTTagInt(rgb));
			}
			
			nbt0.setString("Colors", "<COLORS>");
			
			String list1 = "[";
			for(int i = 0; i < list0.tagCount(); ++i)
			{
				int i0 = list0.getIntAt(i);
				list1 += i0 + ",";
			}
			list1 = list1.substring(0, list1.length() - 1) + "]";
			
			try
			{
				String c = nbt0.toString().replaceAll("\"<COLORS>\"", list1);
				list.appendTag(JsonToNBT.getTagFromJson(c));
			}
			catch(Throwable err) { err.printStackTrace(); }
		}
		
		nbt2.setTag("Explosions", list);
		
		EntityFireworkRocket rocket = new EntityFireworkRocket(world, x, y, z, item);
		if(!world.isRemote) world.spawnEntity(rocket);
	}
}