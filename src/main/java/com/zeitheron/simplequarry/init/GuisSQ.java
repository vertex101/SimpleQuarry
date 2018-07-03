package com.zeitheron.simplequarry.init;

import com.zeitheron.hammercore.client.gui.IGuiCallback;
import com.zeitheron.hammercore.internal.GuiManager;
import com.zeitheron.simplequarry.gui.c.ContainerFilter;
import com.zeitheron.simplequarry.gui.c.ContainerUnification;
import com.zeitheron.simplequarry.gui.g.GuiFilter;
import com.zeitheron.simplequarry.gui.g.GuiUnification;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuisSQ
{
	public static final IGuiCallback FILTER, UNIFICATION;
	
	static
	{
		GuiManager.registerGuiCallback(FILTER = new IGuiCallback()
		{
			@Override
			public Object getClientGuiElement(EntityPlayer player, World world, BlockPos pos)
			{
				return new GuiFilter(player, EnumHand.MAIN_HAND);
			}
			
			@Override
			public Object getServerGuiElement(EntityPlayer player, World world, BlockPos pos)
			{
				return new ContainerFilter(player, player.getHeldItemMainhand());
			}
		});
		
		GuiManager.registerGuiCallback(UNIFICATION = new IGuiCallback()
		{
			@Override
			public Object getClientGuiElement(EntityPlayer player, World world, BlockPos pos)
			{
				return new GuiUnification(player, EnumHand.MAIN_HAND);
			}
			
			@Override
			public Object getServerGuiElement(EntityPlayer player, World world, BlockPos pos)
			{
				return new ContainerUnification(player, player.getHeldItemMainhand());
			}
		});
	}
}