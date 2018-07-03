package com.zeitheron.simplequarry.blocks;

import com.zeitheron.hammercore.internal.GuiManager;
import com.zeitheron.hammercore.internal.blocks.base.BlockDeviceHC;
import com.zeitheron.hammercore.internal.blocks.base.IBlockEnableable;
import com.zeitheron.hammercore.internal.blocks.base.IBlockHorizontal;
import com.zeitheron.hammercore.tile.TileSyncable;
import com.zeitheron.hammercore.utils.WorldUtil;
import com.zeitheron.simplequarry.tile.TilePoweredQuarry;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPoweredQuarry extends BlockDeviceHC<TilePoweredQuarry> implements IBlockHorizontal, IBlockEnableable
{
	public BlockPoweredQuarry()
	{
		super(Material.ROCK, TilePoweredQuarry.class, "powered_quarry");
		setSoundType(SoundType.METAL);
		setHardness(4.5f);
		setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1)
	{
		return new TilePoweredQuarry();
	}
	
	@Override
	public Class<TilePoweredQuarry> getTileClass()
	{
		return TilePoweredQuarry.class;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		GuiManager.openGui(playerIn, WorldUtil.cast(worldIn.getTileEntity(pos), TileSyncable.class));
		return true;
	}
}