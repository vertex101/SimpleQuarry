package com.endie.simplequarry.client.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.world.World;

public class ParticleExtendedDigging extends ParticleDigging
{
	public ParticleExtendedDigging(World w, double x, double y, double z, double mx, double my, double mz, IBlockState bbb)
	{
		super(w, x, y, z, mx, my, mz, bbb);
	}
	
	public int getBrightnessForRender(float pt)
	{
		return 230;
	}
}