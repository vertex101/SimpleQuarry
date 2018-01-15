package com.endie.simplequarry.gui.g;

import org.lwjgl.opengl.GL11;

import com.endie.simplequarry.gui.c.ContainerFuelQuarry;
import com.endie.simplequarry.tile.TileFuelQuarry;
import com.pengu.hammercore.client.texture.gui.DynGuiTex;
import com.pengu.hammercore.client.texture.gui.GuiTexBakery;
import com.pengu.hammercore.client.texture.gui.theme.GuiTheme;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class GuiFuelQuarry extends GuiContainer
{
	final TileFuelQuarry tile;
	
	public GuiFuelQuarry(EntityPlayer player, TileFuelQuarry tile)
	{
		super(new ContainerFuelQuarry(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	public DynGuiTex tex;
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		GuiTexBakery bake = GuiTexBakery.start().body(0, 0, xSize, ySize);
		for(Slot slot : inventorySlots.inventorySlots)
			bake.slot(slot.xPos - 1, slot.yPos - 1);
		tex = bake.bake();
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
		GL11.glEnable(GL11.GL_BLEND);
		tex.render(guiLeft, guiTop);
		
		UtilsFX.bindTexture("textures/gui/def_widgets.png");
		
		int col = GuiTheme.CURRENT_THEME.slotColor;
		GL11.glColor4f(ColorHelper.getRed(col), ColorHelper.getGreen(col), ColorHelper.getBlue(col), 1);
		RenderUtil.drawTexturedModalRect(guiLeft + 81.5, guiTop + 34, 43, 0, 13, 13);
		
		if(tile.totalBurnTicks != 0)
		{
			double fire = (double) tile.burnTicks / tile.totalBurnTicks * 14;
			GL11.glColor4f(1, 1, 1, 1);
			RenderUtil.drawTexturedModalRect(guiLeft + 81, guiTop + 48 - fire, 0, 14 - fire, 14, fire);
		}
	}
}