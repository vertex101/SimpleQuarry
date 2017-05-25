package com.mrdimka.simplequarry.gui.g;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.mrdimka.hammercore.net.pkt.PacketSetProperty;
import com.mrdimka.simplequarry.api.energy.UniversalConverter;
import com.mrdimka.simplequarry.gui.c.ContainerPoweredQuarry;
import com.mrdimka.simplequarry.ref.ModInfo;
import com.mrdimka.simplequarry.tile.TilePoweredQuarry;

public class GuiPoweredQuarry extends GuiContainer
{
	public static ResourceLocation gui = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/gui_powered_quarry.png");
	public static ResourceLocation widgets = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/widgets.png");
	public static ResourceLocation furnace = new ResourceLocation("textures/gui/container/furnace.png");
	TilePoweredQuarry tile;
	
	public GuiPoweredQuarry(TilePoweredQuarry tile, EntityPlayer player)
	{
		super(new ContainerPoweredQuarry(tile, player));
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
		
		mc.getTextureManager().bindTexture(furnace);
		drawTexturedModalRect(guiLeft + 25, guiTop + 33, 56, 36, 14, 14);
		
		double fire = (((double) tile.burnTicks / (double) tile.totalBurnTicks) * 14D);
		RenderUtil.drawTexturedModalRect(guiLeft + 25D, guiTop + 47D - fire, 176D, 14D - fire, 14D, fire);
		
		mc.getTextureManager().bindTexture(widgets);
		
		double power = (((double) tile.storage.getStoredQF(null) / (double) tile.storage.getQFCapacity(null)) * 64D);
		RenderUtil.drawTexturedModalRect(guiLeft + 6D, guiTop + 72D - power, 0D, 17D + 64D - power, 64D, power);
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	@Override
	public void drawScreen(int mx, int my, float p_drawScreen_3_)
	{
		super.drawScreen(mx, my, p_drawScreen_3_);
		
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		
		mc.getTextureManager().bindTexture(gui);
		
//		drawTexturedModalRect(guiLeft + 48, guiTop + 8, 48, 8, 16, 16);
//		if(mx >= guiLeft + 48 && my >= guiTop + 8 && mx < guiLeft + 64 && my < guiTop + 24)
//			drawTexturedModalRect(guiLeft + 48, guiTop + 8, xSize, 0, 16, 16);
//		RenderUtil.drawTexturedModalRect(guiLeft + 48 + 6, guiTop + 8 + 6, 192, tile.listMode ? 0 : 4, 6, 6);
		
		drawTexturedModalRect(guiLeft + 69 + 6, guiTop + 8, 69 + 6, 8, 16, 16);
		if(mx >= guiLeft + 69 + 6 && my >= guiTop + 8 && mx < guiLeft + 69 + 6 + 16 && my < guiTop + 24)
			drawTexturedModalRect(guiLeft + 69 + 6, guiTop + 8, xSize, 16, 16, 16);
		
		drawTexturedModalRect(guiLeft + 69 + 6, guiTop + 27, 69 + 6, 27, 16, 16);
		if(mx >= guiLeft + 69 + 6 && my >= guiTop + 27 && mx < guiLeft + 69 + 6 + 16 && my < guiTop + 27 + 16)
			drawTexturedModalRect(guiLeft + 69 + 6, guiTop + 27, xSize, 32, 16, 16);
		
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		TextureAtlasSprite cobble = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/cobblestone");
		RenderUtil.drawTexturedModalRect(guiLeft + 78, guiTop + 11, cobble, 10, 10);
		
		TextureAtlasSprite dirt = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
		RenderUtil.drawTexturedModalRect(guiLeft + 78, guiTop + 30, dirt, 10, 10);
		
		mc.getTextureManager().bindTexture(gui);
		
		if(!tile.collectCobble.get()) drawTexturedModalRect(guiLeft + 75, guiTop + 8, xSize, 48, 16, 16);
		if(!tile.collectDirt.get()) drawTexturedModalRect(guiLeft + 75, guiTop + 27, xSize, 48, 16, 16);
		
		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft + 61, guiTop + 58, 0);
		GL11.glScaled(1, 1.42, 1);
		RenderUtil.drawTexturedModalRect(0, 0, 62, 45, 100, 13);
		GL11.glPopMatrix();
		
		drawHoveringText(Arrays.asList(I18n.translateToLocal("info." + ModInfo.MOD_ID + ":collect." + (tile.collectCobble.get() ? "yes" : "no"))), guiLeft + 89, guiTop + 24);
		drawHoveringText(Arrays.asList(I18n.translateToLocal("info." + ModInfo.MOD_ID + ":collect." + (tile.collectDirt.get() ? "yes" : "no"))), guiLeft + 89, guiTop + 43);
		
		String b = new DecimalFormat("#0").format(tile.storage.getStoredQF(null) / (UniversalConverter.FT_QF(1600) / tile.BPC));
		if(mx - guiLeft >= 6 && my - guiTop >= 7 && mx - guiLeft <= 6 + 13 && my - guiTop <= 7 + 66)
			drawHoveringText(Arrays.asList(b + " " + I18n.translateToLocal("info." + ModInfo.MOD_ID + ":blockstobreak")), guiLeft + 16, guiTop + 48);
		
//		if(mx - guiLeft >= 62 && my - guiTop >= 59 && mx - guiLeft <= 150 && my - guiTop <= 75)
//			drawHoveringText(Arrays.asList("Psss, don't worry!", "Upgrades aren't coming any time soon!"), guiLeft + 48, guiTop + 57);
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	@Override
	protected void mouseClicked(int mx, int my, int mouseButton) throws IOException
	{
		super.mouseClicked(mx, my, mouseButton);
		
		if(mx >= guiLeft + 48 && my >= guiTop + 8 && mx < guiLeft + 64 && my < guiTop + 24)
		{
//			System.out.println("LMode");
		}
		
		if(mx >= guiLeft + 69 + 6 && my >= guiTop + 8 && mx < guiLeft + 69 + 6 + 16 && my < guiTop + 24)
		{
			tile.collectCobble.set(!tile.collectCobble.get());
			PacketSetProperty.toServer(tile, tile.collectCobble);
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}
		
		if(mx >= guiLeft + 69 + 6 && my >= guiTop + 27 && mx < guiLeft + 69 + 6 + 16 && my < guiTop + 27 + 16)
		{
			tile.collectDirt.set(!tile.collectDirt.get());
			PacketSetProperty.toServer(tile, tile.collectDirt);
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}
	}
}