package com.mrdimka.simplequarry.gui.g;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.mrdimka.simplequarry.gui.c.ContainerFuelQuarry;
import com.mrdimka.simplequarry.ref.ModInfo;
import com.mrdimka.simplequarry.tile.TileFuelQuarry;

public class GuiFuelQuarry extends GuiContainer
{
	public static ResourceLocation gui = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/gui_fuel_quarry.png");
	public static ResourceLocation furnace = new ResourceLocation("textures/gui/container/furnace.png");
	TileFuelQuarry tile;
	
	public GuiFuelQuarry(TileFuelQuarry tile, EntityPlayer player)
	{
		super(new ContainerFuelQuarry(tile, player));
		this.tile = tile;
		
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int mx, int my)
	{
		GL11.glEnable(GL11.GL_BLEND);
		
		mc.getTextureManager().bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		drawTexturedModalRect(guiLeft + 63, guiTop + 32, xSize, 26, 18, 18);
		drawTexturedModalRect(guiLeft + 132, guiTop + 28, xSize, 0, 26, 26);
		
		drawTexturedModalRect(guiLeft + 7, guiTop + 8, 194, 44, 18, 65);
		
		mc.getTextureManager().bindTexture(furnace);
		drawTexturedModalRect(guiLeft + 81, guiTop + 33, 56, 36, 14, 14);
		
		double fire = (((double) tile.burnTicks / (double) tile.totalBurnTicks) * 14D);
		RenderUtil.drawTexturedModalRect(guiLeft + 81D, guiTop + 47D - fire, 176D, 14D - fire, 14D, fire);
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
}