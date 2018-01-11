package com.endie.simplequarry.cfg;

import java.util.Set;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.cfg.gui.HCConfigGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ConfigFactorySQ implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
		HammerCore.LOG.info("Created Simple Quarry Gui Config Factory!", new Object[0]);
	}
	
	@Override
	public boolean hasConfigGui()
	{
		return true;
	}
	
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new HCConfigGui(parentScreen, ConfigsSQ.cfgs, "simplequarry");
	}
	
	@Override
	public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
}