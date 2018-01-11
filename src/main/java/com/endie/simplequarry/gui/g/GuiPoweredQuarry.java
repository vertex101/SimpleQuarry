package com.endie.simplequarry.gui.g;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.endie.simplequarry.InfoSQ;
import com.endie.simplequarry.api.energy.UniversalConverter;
import com.endie.simplequarry.cfg.ConfigsSQ;
import com.endie.simplequarry.gui.c.ContainerPoweredQuarry;
import com.endie.simplequarry.proxy.ClientProxy;
import com.endie.simplequarry.tile.TilePoweredQuarry;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.net.pkt.PacketSetProperty;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;

public class GuiPoweredQuarry extends GuiContainer
{
	public static ResourceLocation gui = new ResourceLocation(InfoSQ.MOD_ID, "textures/gui/gui_powered_quarry.png");
	public static ResourceLocation widgets = new ResourceLocation(InfoSQ.MOD_ID, "textures/gui/widgets.png");
	public static ResourceLocation furnace = new ResourceLocation("textures/gui/container/furnace.png");
	final TilePoweredQuarry tile;
	
	public GuiPoweredQuarry(EntityPlayer player, TilePoweredQuarry tile)
	{
		super(new ContainerPoweredQuarry(player, tile));
		this.tile = tile;
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float pt, int mx, int my)
	{
		GL11.glEnable(3042);
		mc.getTextureManager().bindTexture(gui);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		mc.getTextureManager().bindTexture(furnace);
		drawTexturedModalRect(guiLeft + 25, guiTop + 33, 56, 36, 14, 14);
		
		if(tile.totalBurnTicks != 0)
		{
			double fire = (double) tile.burnTicks / tile.totalBurnTicks * 14;
			RenderUtil.drawTexturedModalRect(guiLeft + 25, guiTop + 47 - fire, 176, 14 - fire, 14, fire);
		}
		
		mc.getTextureManager().bindTexture(widgets);
		
		double power = tile.storage.getStoredQF(null) / tile.storage.getQFCapacity(null) * 64;
		RenderUtil.drawTexturedModalRect(guiLeft + 6, guiTop + 72 - power, 0, 81 - power, 64, power);
	}
	
	@Override
	public void drawScreen(int mx, int my, float p_drawScreen_3_)
	{
		drawDefaultBackground();
		super.drawScreen(mx, my, p_drawScreen_3_);
		renderHoveredToolTip(mx, my);
		
		GL11.glDisable(2896);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		if(mx - guiLeft >= 6 && my - guiTop >= 7 && mx - guiLeft <= 19 && my - guiTop <= 73)
			drawHoveringText(Arrays.asList(new DecimalFormat("#0").format(tile.storage.getStoredQF(null) / (UniversalConverter.FT_QF(TileEntityFurnace.getItemBurnTime(ClientProxy.COAL)) / ConfigsSQ.BLOCKS_PER_COAL)) + " " + I18n.format("info.simplequarry:blockstobreak")), guiLeft + 16, guiTop + 48);
	}
	
	@Override
	protected void mouseClicked(int mx, int my, int mouseButton) throws IOException
	{
		super.mouseClicked(mx, my, mouseButton);
	}
}