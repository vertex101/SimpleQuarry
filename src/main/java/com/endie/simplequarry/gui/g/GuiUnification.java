package com.endie.simplequarry.gui.g;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.endie.simplequarry.InfoSQ;
import com.endie.simplequarry.gui.c.ContainerUnification;
import com.endie.simplequarry.init.ItemsSQ;
import com.pengu.hammercore.client.utils.UtilsFX;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
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
		UtilsFX.bindTexture(InfoSQ.MOD_ID, "textures/gui/gui_unification.png");
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		for(Slot slot : inventorySlots.inventorySlots)
			if(slot.inventory instanceof InventoryPlayer)
				drawTexturedModalRect(guiLeft - 1 + slot.xPos, guiTop - 1 + slot.yPos, xSize, 0, 18, 18);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(typedChar >= '0' && typedChar <= '9')
			return;
		super.keyTyped(typedChar, keyCode);
	}
}