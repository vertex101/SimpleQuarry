package com.endie.simplequarry.client.particle;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.SimpleParticle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleBlock extends SimpleParticle
{
	public final IBlockState state;
	public BlockPos pos;
	
	public ParticleBlock(World worldIn, double posXIn, double posYIn, double posZIn, IBlockState state)
	{
		super(worldIn, posXIn + .5, posYIn, posZIn + .5);
		particleMaxAge = 250;
		this.state = state;
		pos = new BlockPos(posXIn, posYIn, posZIn);
	}
	
	@Override
	public int getBrightnessForRender(float pt)
	{
		return 230;
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		if(isExpired)
			return;
		
		GL11.glPushMatrix();
		float scale = (particleMaxAge - (particleAge + partialTicks)) / particleMaxAge;
		GL11.glTranslated(x - .8 * scale, y - .7 * scale, z - .8 * scale);
		GL11.glScaled(.7 * scale, .7 * scale, .7 * scale);
		
		int rotate = Math.abs(hashCode()) % 3050;
		
		GL11.glTranslated(.5, .5, .5);
		GL11.glRotated(scale * 120 + rotate, 0, 1, 0);
		GL11.glRotated(scale * 120.5 - rotate * .5, 1, 0, 0);
		GL11.glTranslated(-.5, -.5, -.5);
		
		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		BufferBuilder vb = Tessellator.getInstance().getBuffer();
		try
		{
			vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			if(state != null)
				brd.renderBlock(state, BlockPos.ORIGIN, world, vb);
			Tessellator.getInstance().draw();
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		GL11.glPopMatrix();
	}
}