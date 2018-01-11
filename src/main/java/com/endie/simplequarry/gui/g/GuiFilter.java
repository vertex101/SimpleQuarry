package com.endie.simplequarry.gui.g;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.endie.simplequarry.InfoSQ;
import com.endie.simplequarry.gui.c.ContainerFilter;
import com.endie.simplequarry.gui.c.ContainerFilter.FilterData;
import com.pengu.hammercore.client.utils.UtilsFX;

import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;

public class GuiFilter extends GuiContainer
{
	public GuiFilter(EntityPlayer player, EnumHand hand)
	{
		super(new ContainerFilter(player, player.getHeldItem(hand)));
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
		
		FilterData filter = ((ContainerFilter) inventorySlots).t;
		
		{
			boolean hover = mouseX >= guiLeft + 18 && mouseY >= guiTop + 17 && mouseX < guiLeft + 34 && mouseY < guiTop + 33;
			if(hover)
				drawHoveringText(I18n.format("info.simplequarry:filter." + (filter.invert ? "whitelist" : "blacklist")), mouseX, mouseY);
		}
		
		{
			boolean hover = mouseX >= guiLeft + 18 && mouseY >= guiTop + 17 + 18 && mouseX < guiLeft + 34 && mouseY < guiTop + 33 + 18;
			if(hover)
				drawHoveringText(I18n.format("info.simplequarry:filter.oredict." + (filter.useod ? "yes" : "no")), mouseX, mouseY);
		}
		
		{
			boolean hover = mouseX >= guiLeft + 18 && mouseY >= guiTop + 17 + 36 && mouseX < guiLeft + 34 && mouseY < guiTop + 33 + 36;
			if(hover)
				drawHoveringText(I18n.format("info.simplequarry:filter.meta." + (filter.usemeta ? "yes" : "no")), mouseX, mouseY);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		UtilsFX.bindTexture(InfoSQ.MOD_ID, "textures/gui/gui_filter.png");
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		for(Slot slot : inventorySlots.inventorySlots)
			drawTexturedModalRect(guiLeft - 1 + slot.xPos, guiTop - 1 + slot.yPos, xSize, 0, 18, 18);
		
		GL11.glColor4f(1, 1, 1, 1);
		
		FilterData filter = ((ContainerFilter) inventorySlots).t;
		
		UtilsFX.bindTexture(InfoSQ.MOD_ID, "textures/gui/gui_filter.png");
		
		{
			boolean hover = mouseX >= guiLeft + 18 && mouseY >= guiTop + 17 && mouseX < guiLeft + 34 && mouseY < guiTop + 33;
			
			drawTexturedModalRect(guiLeft + 18, guiTop + 17, xSize + (hover ? 16 : 0), 18 + (filter.invert ? 16 : 0), 16, 16);
		}
		
		{
			boolean hover = mouseX >= guiLeft + 18 && mouseY >= guiTop + 17 + 18 && mouseX < guiLeft + 34 && mouseY < guiTop + 33 + 18;
			
			drawTexturedModalRect(guiLeft + 18, guiTop + 35, xSize + (hover ? 16 : 0), 50 + (filter.useod ? 0 : 16), 16, 16);
		}
		
		{
			boolean hover = mouseX >= guiLeft + 18 && mouseY >= guiTop + 17 + 36 && mouseX < guiLeft + 34 && mouseY < guiTop + 33 + 36;
			
			drawTexturedModalRect(guiLeft + 18, guiTop + 53, xSize + (hover ? 16 : 0), 82 + (filter.usemeta ? 0 : 16), 16, 16);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(typedChar >= '0' && typedChar <= '9')
			return;
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(mouseX >= guiLeft + 18 && mouseY >= guiTop + 17 && mouseX < guiLeft + 34 && mouseY < guiTop + 33 && inventorySlots.enchantItem(mc.player, 0))
		{
			mc.playerController.sendEnchantPacket(inventorySlots.windowId, 0);
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
		}
		
		if(mouseX >= guiLeft + 18 && mouseY >= guiTop + 17 + 18 && mouseX < guiLeft + 34 && mouseY < guiTop + 33 + 18 && inventorySlots.enchantItem(mc.player, 1))
		{
			mc.playerController.sendEnchantPacket(inventorySlots.windowId, 1);
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
		}
		
		if(mouseX >= guiLeft + 18 && mouseY >= guiTop + 17 + 36 && mouseX < guiLeft + 34 && mouseY < guiTop + 33 + 36 && inventorySlots.enchantItem(mc.player, 2))
		{
			mc.playerController.sendEnchantPacket(inventorySlots.windowId, 2);
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}