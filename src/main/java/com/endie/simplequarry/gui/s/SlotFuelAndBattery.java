/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.pengu.hammercore.energy.iPowerContainerItem
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.inventory.SlotFurnaceFuel
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumFacing
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.energy.CapabilityEnergy
 */
package com.endie.simplequarry.gui.s;

import com.pengu.hammercore.energy.iPowerContainerItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class SlotFuelAndBattery
extends SlotFurnaceFuel {
    public SlotFuelAndBattery(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        if (stack.getItem() instanceof iPowerContainerItem || stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
            return true;
        }
        return super.isItemValid(stack);
    }
}
