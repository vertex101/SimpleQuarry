package com.zeitheron.simplequarry.gui.g;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.client.utils.texture.gui.DynGuiTex;
import com.zeitheron.hammercore.client.utils.texture.gui.GuiTexBakery;
import com.zeitheron.hammercore.client.utils.texture.gui.theme.GuiTheme;
import com.zeitheron.hammercore.utils.color.ColorHelper;
import com.zeitheron.simplequarry.InfoSQ;
import com.zeitheron.simplequarry.gui.c.ContainerUnification;
import com.zeitheron.simplequarry.init.ItemsSQ;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class GuiUnification extends GuiContainer
{
	public GuiUnification(EntityPlayer player, EnumHand hand)
	{
		super(new ContainerUnification(player, player.getHeldItem(hand)));
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
		
		Slot filter = inventorySlots.getSlot(0);
		if(isMouseOverSlot(filter, mouseX, mouseY) && !filter.getHasStack())
			drawHoveringText(ItemsSQ.UPGRADE_FILTER.getItemStackDisplayName(ItemStack.EMPTY), mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		tex.render(guiLeft, guiTop);
		
		int col = GuiTheme.CURRENT_THEME.bodyLayerLU;
		GL11.glColor4f(ColorHelper.getRed(col), ColorHelper.getGreen(col), ColorHelper.getBlue(col), .5F);
		GL11.glEnable(GL11.GL_BLEND);
		UtilsFX.bindTexture(InfoSQ.MOD_ID, "textures/gui/widgets.png");
		drawTexturedModalRect(guiLeft + 81, guiTop + 50, 32, 0, 14, 14);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(typedChar >= '0' && typedChar <= '9')
			return;
		super.keyTyped(typedChar, keyCode);
	}
}