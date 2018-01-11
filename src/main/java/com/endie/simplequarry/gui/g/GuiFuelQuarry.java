package com.endie.simplequarry.gui.g;

import org.lwjgl.opengl.GL11;

import com.endie.simplequarry.InfoSQ;
import com.endie.simplequarry.gui.c.ContainerFuelQuarry;
import com.endie.simplequarry.tile.TileFuelQuarry;
import com.pengu.hammercore.client.utils.RenderUtil;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFuelQuarry extends GuiContainer
{
	public static ResourceLocation gui = new ResourceLocation(InfoSQ.MOD_ID, "textures/gui/gui_fuel_quarry.png");
	public static ResourceLocation furnace = new ResourceLocation("textures/gui/container/furnace.png");
	final TileFuelQuarry tile;
	
	public GuiFuelQuarry(EntityPlayer player, TileFuelQuarry tile)
	{
		super(new ContainerFuelQuarry(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		GL11.glColor4f(1, 1, 1, 1);
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float pt, int mx, int my)
	{
		GL11.glEnable(3042);
		mc.getTextureManager().bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawTexturedModalRect(guiLeft + 63, guiTop + 32, xSize, 26, 18, 18);
		drawTexturedModalRect(guiLeft + 132, guiTop + 28, xSize, 0, 26, 26);
		drawTexturedModalRect(guiLeft + 7, guiTop + 8, 194, 44, 18, 65);
		mc.getTextureManager().bindTexture(furnace);
		drawTexturedModalRect(guiLeft + 81, guiTop + 33, 56, 36, 14, 14);
		if(tile.totalBurnTicks != 0)
		{
			double fire = (double) tile.burnTicks / tile.totalBurnTicks * 14;
			
			RenderUtil.drawTexturedModalRect(guiLeft + 81.0, guiTop + 47.0 - fire, 176.0, 14.0 - fire, 14.0, fire);
		}
	}
}