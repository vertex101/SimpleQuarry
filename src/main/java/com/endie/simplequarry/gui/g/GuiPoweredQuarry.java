package com.endie.simplequarry.gui.g;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.endie.simplequarry.api.energy.UniversalConverter;
import com.endie.simplequarry.cfg.ConfigsSQ;
import com.endie.simplequarry.gui.c.ContainerPoweredQuarry;
import com.endie.simplequarry.proxy.ClientProxy;
import com.endie.simplequarry.tile.TilePoweredQuarry;
import com.pengu.hammercore.client.texture.gui.DynGuiTex;
import com.pengu.hammercore.client.texture.gui.GuiTexBakery;
import com.pengu.hammercore.client.texture.gui.theme.GuiTheme;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.color.InterpolationUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class GuiPoweredQuarry extends GuiContainer
{
	final TilePoweredQuarry tile;
	
	public GuiPoweredQuarry(EntityPlayer player, TilePoweredQuarry tile)
	{
		super(new ContainerPoweredQuarry(player, tile));
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
		bake.slot(6, 7, 13, 66);
		tex = bake.bake();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float pt, int mx, int my)
	{
		GL11.glEnable(GL11.GL_BLEND);
		tex.render(guiLeft, guiTop);
		
		UtilsFX.bindTexture("textures/gui/def_widgets.png");
		
		int col = GuiTheme.CURRENT_THEME.slotColor;
		GL11.glColor4f(ColorHelper.getRed(col), ColorHelper.getGreen(col), ColorHelper.getBlue(col), 1);
		RenderUtil.drawTexturedModalRect(guiLeft + 26.5, guiTop + 34, 43, 0, 13, 13);
		
		if(tile.totalBurnTicks != 0)
		{
			double fire = (double) tile.burnTicks / tile.totalBurnTicks * 14;
			GL11.glColor4f(1, 1, 1, 1);
			RenderUtil.drawTexturedModalRect(guiLeft + 25, guiTop + 47 - fire, 0, 14 - fire, 14, fire);
		}
		
		float power = (float) (tile.storage.getStoredQF(null) / tile.storage.getQFCapacity(null) * 64);
		int finalCol = InterpolationUtil.interpolate(0xFF803400, 0xFFFE6A00, (float) power / 64F);
		
		GL11.glColor4f(1, 1, 1, 1);
		RenderUtil.drawGradientRect(guiLeft + 7, guiTop + 72 - power, 11, power, finalCol, 0xFF803400);
		// GuiWidgets.drawEnergy(guiLeft + 7, guiTop + 72 - power, 11, power,
		// EnumPowerAnimation.UP);
	}
	
	private static final DecimalFormat df = new DecimalFormat("#0");
	
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
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			RenderUtil.drawColoredModalRect(guiLeft + 7, guiTop + 8, 11, 64, 0x80FFFFFF);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			ItemStack mouse = HCNetwork.getMouseStack(mc.player);
			if(mouse.isEmpty())
				drawHoveringText(Arrays.asList(df.format(tile.storage.getStoredQF(null) / (UniversalConverter.FT_QF(TileEntityFurnace.getItemBurnTime(ClientProxy.COAL)) / ConfigsSQ.BLOCKS_PER_COAL)) + " " + I18n.format("info.simplequarry:blockstobreak")), guiLeft + 16, guiTop + 48);
		}
	}
}