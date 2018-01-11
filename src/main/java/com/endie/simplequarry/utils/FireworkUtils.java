package com.endie.simplequarry.utils;

import java.util.Random;

import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class FireworkUtils
{
	public static ItemStack makeRandomFirework(Random rand)
	{
		ItemStack item = new ItemStack(Items.FIREWORKS);
		NBTTagCompound nbt = new NBTTagCompound();
		item.setTagCompound(nbt);
		NBTTagCompound nbt2 = new NBTTagCompound();
		nbt.setTag("Fireworks", nbt2);
		nbt2.setInteger("Flight", 1);
		NBTTagList list = new NBTTagList();
		for(int o = 0; o < 1; ++o)
		{
			NBTTagCompound nbt0 = new NBTTagCompound();
			nbt0.setBoolean("Trail", rand.nextBoolean());
			nbt0.setByte("Type", (byte) rand.nextInt(8));
			NBTTagList list0 = new NBTTagList();
			for(int i = 0; i < 6 + rand.nextInt(6); ++i)
				list0.appendTag(new NBTTagInt(ColorHelper.packRGB(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())));
			nbt0.setString("Colors", "<COLORS>");
			String list1 = "[";
			for(int i = 0; i < list0.tagCount(); ++i)
			{
				int i0 = list0.getIntAt(i);
				list1 = list1 + i0 + ",";
			}
			list1 = list1.substring(0, list1.length() - 1) + "]";
			try
			{
				list.appendTag(JsonToNBT.getTagFromJson(nbt0.toString().replaceAll("\"<COLORS>\"", list1)));
				continue;
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		}
		nbt2.setTag("Explosions", list);
		return item;
	}
	
	public static void spawnRandomFirework(World world, double x, double y, double z)
	{
		EntityFireworkRocket rocket = new EntityFireworkRocket(world, x, y, z, makeRandomFirework(world.rand));
		if(!world.isRemote)
			world.spawnEntity(rocket);
	}
}