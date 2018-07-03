package com.zeitheron.simplequarry.cfg;

import java.util.Set;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.cfg.gui.HCConfigGui;
import com.zeitheron.simplequarry.InfoSQ;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ConfigFactorySQ implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
		HammerCore.LOG.info("Created Simple Quarry Gui Config Factory!");
	}
	
	@Override
	public boolean hasConfigGui()
	{
		return true;
	}
	
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new HCConfigGui(parentScreen, ConfigsSQ.cfgs, InfoSQ.MOD_ID);
	}
	
	@Override
	public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
}