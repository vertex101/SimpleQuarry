/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.pengu.hammercore.client.particle.api.ParticleList
 *  com.pengu.hammercore.math.MathHelper
 *  com.pengu.hammercore.proxy.ParticleProxy_Client
 *  com.pengu.hammercore.utils.WorldLocation
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 */
package com.endie.simplequarry.vortex;

import com.endie.simplequarry.SimpleQuarry;
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

public class QuarryVortex
extends Vortex {
    public WorldLocation loc;
    public final TileFuelQuarry quarry;
    public int ticksExisted = 0;

    public QuarryVortex(TileFuelQuarry quarry) {
        super((double)quarry.getPos().getX() + 0.5, (double)quarry.getPos().getY() + 0.5, (double)quarry.getPos().getZ() + 0.5, 16.0, false);
        this.loc = quarry.getLocation();
        this.quarry = quarry;
    }

    @Override
    public void update() {
        double my;
        Particle p;
        double py;
        int i;
        double pz;
        double mx;
        double px;
        double mz;
        if (this.loc.getTileOfType(TileFuelQuarry.class) != this.quarry) {
            SimpleQuarry.proxy.removeParticleVortex(this);
            return;
        }
        World world = this.loc.getWorld();
        BlockPos pos = this.loc.getPos();
        if (this.getBoundingBox() == null) {
            return;
        }
        List particles = ParticleList.getParticlesWithinAABB((AxisAlignedBB)this.getBoundingBox());
        for (i = 0; i < particles.size(); ++i) {
            p = (Particle)particles.get(i);
            mx = ParticleProxy_Client.getParticleMotionX((Particle)p);
            my = ParticleProxy_Client.getParticleMotionY((Particle)p);
            mz = ParticleProxy_Client.getParticleMotionZ((Particle)p);
            px = ParticleProxy_Client.getParticlePosX((Particle)p);
            py = ParticleProxy_Client.getParticlePosY((Particle)p);
            pz = ParticleProxy_Client.getParticlePosZ((Particle)p);
            double dx = px - this.x;
            double dy = py - this.y;
            double dz = pz - this.z;
            double distX = Math.sqrt(dx * dx);
            double distY = Math.sqrt(dy * dy);
            double distZ = Math.sqrt(dz * dz);
            mx = MathHelper.clip((double)(this.x - px), (double)-1.0, (double)1.0) / (8.0 / distX);
            my = MathHelper.clip((double)(this.y - py - 0.5), (double)-1.0, (double)1.0) / (8.0 / distY);
            mz = MathHelper.clip((double)(this.z - pz), (double)-1.0, (double)1.0) / (8.0 / distZ);
            ParticleProxy_Client.setParticleMotionX((Particle)p, (double)mx);
            ParticleProxy_Client.setParticleMotionY((Particle)p, (double)my);
            ParticleProxy_Client.setParticleMotionZ((Particle)p, (double)mz);
        }
        particles = ParticleList.getParticlesWithinAABB((AxisAlignedBB)new AxisAlignedBB(pos.down()));
        for (i = 0; i < particles.size(); ++i) {
            p = (Particle)particles.get(i);
            mx = ParticleProxy_Client.getParticleMotionX((Particle)p);
            my = ParticleProxy_Client.getParticleMotionY((Particle)p);
            mz = ParticleProxy_Client.getParticleMotionZ((Particle)p);
            px = ParticleProxy_Client.getParticlePosX((Particle)p);
            py = ParticleProxy_Client.getParticlePosY((Particle)p);
            pz = ParticleProxy_Client.getParticlePosZ((Particle)p);
            mx = MathHelper.clip((double)(this.x - px), (double)-1.0, (double)1.0) / 8.0;
            my = MathHelper.clip((double)(this.y - py + 1.0), (double)-1.0, (double)1.0) / 8.0;
            mz = MathHelper.clip((double)(this.z - pz), (double)-1.0, (double)1.0) / 8.0;
            ParticleProxy_Client.setParticleMotionX((Particle)p, (double)mx);
            ParticleProxy_Client.setParticleMotionY((Particle)p, (double)my);
            ParticleProxy_Client.setParticleMotionZ((Particle)p, (double)mz);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return this.quarry.boundingBox;
    }
}
