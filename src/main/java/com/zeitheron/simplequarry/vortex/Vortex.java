/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ITickable
 *  net.minecraft.util.math.AxisAlignedBB
 */
package com.zeitheron.simplequarry.vortex;

import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class Vortex
implements ITickable {
    private final int hashCode;
    protected double x;
    protected double y;
    protected double z;
    protected double vortexStrenght;
    protected double radius = 1.0;
    protected AxisAlignedBB boundingBox;

    public Vortex(double x, double y, double z, double vortexStrenght, boolean includeStrenghtInHash) {
        this.hashCode = ("" + x + "," + y + "," + z + "x" + (includeStrenghtInHash ? vortexStrenght : 0.0)).hashCode();
        this.vortexStrenght = vortexStrenght;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vortex(double x, double y, double z, double vortexStrenght, double radius, boolean includeStrenghtInHash) {
        this(x, y, z, vortexStrenght, includeStrenghtInHash);
        this.radius = radius;
        this.rebuildBoundingBox();
    }

    public AxisAlignedBB rebuildBoundingBox() {
        this.boundingBox = new AxisAlignedBB(this.x - this.radius, this.y - this.radius, this.z - this.radius, this.x + this.radius, this.y + this.radius, this.z + this.radius);
        return this.boundingBox;
    }

    public AxisAlignedBB getBoundingBox() {
        if (this.boundingBox == null || this.boundingBox.maxX - this.boundingBox.minX != this.radius * 2.0 || this.boundingBox.maxX - this.radius != this.x || this.boundingBox.maxY - this.radius != this.y || this.boundingBox.maxZ - this.radius != this.z) {
            this.rebuildBoundingBox();
        }
        return this.boundingBox;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getVortexStrenght() {
        return this.vortexStrenght;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void update() {
    }

    public int hashCode() {
        return this.getHashCode();
    }
}
