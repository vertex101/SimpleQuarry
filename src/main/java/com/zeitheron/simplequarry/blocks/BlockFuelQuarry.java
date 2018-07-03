package com.zeitheron.simplequarry.blocks;

import com.zeitheron.hammercore.internal.GuiManager;
import com.zeitheron.hammercore.internal.blocks.base.BlockDeviceHC;
import com.zeitheron.hammercore.internal.blocks.base.IBlockEnableable;
import com.zeitheron.hammercore.internal.blocks.base.IBlockHorizontal;
import com.zeitheron.hammercore.tile.TileSyncable;
import com.zeitheron.hammercore.utils.WorldUtil;
import com.zeitheron.simplequarry.tile.TileFuelQuarry;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFuelQuarry extends BlockDeviceHC<TileFuelQuarry> implements IBlockHorizontal, IBlockEnableable
{
	public BlockFuelQuarry()
	{
		super(Material.ROCK, TileFuelQuarry.class, "fuel_quarry");
		setSoundType(SoundType.STONE);
		setHardness(4.0f);
		setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		GuiManager.openGui(playerIn, WorldUtil.cast(worldIn.getTileEntity(pos), TileSyncable.class));
		return true;
	}
}