package com.endie.simplequarry.vortex;

import com.endie.simplequarry.SimpleQuarry;
import com.endie.simplequarry.client.particle.ParticleBlock;
import com.endie.simplequarry.proxy.CommonProxy;
import com.endie.simplequarry.tile.TileFuelQuarry;
import com.endie.simplequarry.vortex.Vortex;
import com.pengu.hammercore.client.particle.api.ParticleList;
import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.hammercore.utils.WorldLocation;

import java.util.List;
import net.minecraft.client.particle.Particle;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class QuarryVortex extends Vortex
{
	public WorldLocation loc;
	public final TileFuelQuarry quarry;
	public int ticksExisted = 0;
	
	public QuarryVortex(TileFuelQuarry quarry)
	{
		super(quarry.getPos().getX() + .5, quarry.getPos().getY() + .5, quarry.getPos().getZ() + .5, 16.0, false);
		this.loc = quarry.getLocation();
		this.quarry = quarry;
	}
	
	@Override
	public void update()
	{
		double my;
		Particle p;
		double py;
		int i;
		double pz;
		double mx;
		double px;
		double mz;
		if(this.loc.getTileOfType(TileFuelQuarry.class) != this.quarry)
		{
			SimpleQuarry.proxy.removeParticleVortex(this);
			return;
		}
		World world = this.loc.getWorld();
		BlockPos pos = this.loc.getPos();
		if(this.getBoundingBox() == null)
		{
			return;
		}
		List<Particle> particles = ParticleList.getParticlesWithinAABB(getBoundingBox());
		for(i = 0; i < particles.size(); ++i)
		{
			p = particles.get(i);
			mx = ParticleProxy_Client.getParticleMotionX(p);
			my = ParticleProxy_Client.getParticleMotionY(p);
			mz = ParticleProxy_Client.getParticleMotionZ(p);
			px = ParticleProxy_Client.getParticlePosX(p);
			py = ParticleProxy_Client.getParticlePosY(p);
			pz = ParticleProxy_Client.getParticlePosZ(p);
			double dx = px - this.x;
			double dy = py - this.y;
			double dz = pz - this.z;
			double distX = Math.sqrt(dx * dx);
			double distY = Math.sqrt(dy * dy);
			double distZ = Math.sqrt(dz * dz);
			mx = MathHelper.clip((this.x - px), -1.0, 1.0) / (8.0 / distX);
			my = MathHelper.clip((this.y - py - .7), -1.0, 1.0) / (8.0 / distY);
			mz = MathHelper.clip((this.z - pz), -1.0, 1.0) / (8.0 / distZ);
			ParticleProxy_Client.setParticleMotionX(p, mx);
			ParticleProxy_Client.setParticleMotionY(p, my);
			ParticleProxy_Client.setParticleMotionZ(p, mz);
		}
		particles = ParticleList.getParticlesWithinAABB(new AxisAlignedBB(pos.down()));
		for(i = 0; i < particles.size(); ++i)
		{
			p = particles.get(i);
			mx = ParticleProxy_Client.getParticleMotionX(p);
			my = ParticleProxy_Client.getParticleMotionY(p);
			mz = ParticleProxy_Client.getParticleMotionZ(p);
			px = ParticleProxy_Client.getParticlePosX(p);
			py = ParticleProxy_Client.getParticlePosY(p);
			pz = ParticleProxy_Client.getParticlePosZ(p);
			mx = MathHelper.clip(x - px, -1.0, 1.0) / 8.0;
			my = MathHelper.clip(y - py + 1.0, -1.0, 1.0) / 8.0;
			mz = MathHelper.clip(z - pz, -1.0, 1.0) / 8.0;
			p.motionX = mx;
			p.motionY = my;
			p.motionZ = mz;
			
			if(p instanceof ParticleBlock)
			{
				ParticleBlock pb = (ParticleBlock) p;
				pb.particleAge += 10;
				if(pb.particleAge >= pb.particleMaxAge)
					pb.setExpired();
			}
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox()
	{
		return this.quarry.boundingBox;
	}
}
