package com.endie.simplequarry.init;

import com.endie.simplequarry.gui.c.ContainerFilter;
import com.endie.simplequarry.gui.c.ContainerUnification;
import com.endie.simplequarry.gui.g.GuiFilter;
import com.endie.simplequarry.gui.g.GuiUnification;
import com.pengu.hammercore.core.gui.GuiManager;
import com.pengu.hammercore.core.gui.iGuiCallback;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuisSQ
{
	public static final iGuiCallback FILTER, UNIFICATION;
	
	static
	{
		GuiManager.registerGuiCallback(FILTER = new iGuiCallback()
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
		
		GuiManager.registerGuiCallback(UNIFICATION = new iGuiCallback()
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