package com.endie.simplequarry.blocks;

import com.endie.simplequarry.tile.TileFuelQuarry;
import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.common.EnumRotation;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.core.gui.GuiManager;
import com.pengu.hammercore.tile.TileSyncable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockFuelQuarry extends Block implements ITileEntityProvider, iTileBlock<TileFuelQuarry>
{
	public static final PropertyBool IS_MINING = PropertyBool.create((String) "mining");
	
	public BlockFuelQuarry()
	{
		super(Material.ROCK);
		setSoundType(SoundType.STONE);
		setUnlocalizedName("fuel_quarry");
		setHardness(4.0f);
		setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1)
	{
		return new TileFuelQuarry();
	}
	
	@Override
	public Class<TileFuelQuarry> getTileClass()
	{
		return TileFuelQuarry.class;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		GuiManager.openGui(playerIn, WorldUtil.cast(worldIn.getTileEntity(pos), TileSyncable.class));
		return true;
	}
	
	@Override
	public boolean rotateBlock(World w, BlockPos p, EnumFacing s)
	{
		int meta = w.getBlockState(p).getBlock().getMetaFromState(w.getBlockState(p)) + 1;
		if(meta > 3)
			meta = 0;
		TileEntity te = w.getTileEntity(p);
		w.setBlockState(p, this.getStateFromMeta(meta));
		if(te != null)
		{
			te.validate();
			w.setTileEntity(p, te);
			te.markDirty();
		}
		return true;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		int meta0 = (int) Math.floor((double) meta / 2.0);
		int meta1 = meta % 2;
		return this.getDefaultState().withProperty((IProperty) EnumRotation.FACING, (Comparable) EnumRotation.values()[meta0 % EnumRotation.values().length]).withProperty((IProperty) IS_MINING, (Comparable) Boolean.valueOf(true));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int meta0 = ((EnumRotation) state.getValue((IProperty) EnumRotation.FACING)).ordinal();
		boolean meta1 = (Boolean) state.getValue((IProperty) IS_MINING);
		return meta0 == 0 && meta1 ? 0 : (meta0 == 0 && !meta1 ? 1 : (meta0 == 1 && meta1 ? 2 : (meta0 == 1 && !meta1 ? 3 : (meta0 == 2 && meta1 ? 4 : (meta0 == 2 && !meta1 ? 5 : (meta0 == 3 && meta1 ? 6 : (meta0 == 3 && !meta1 ? 7 : 0)))))));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, EnumRotation.FACING, IS_MINING);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase ent, ItemStack stack)
	{
		TileEntity te;
		int l = MathHelper.floor(ent.rotationYaw * 4 / 360 + .5) & 3;
		int meta = 0;
		if(l == 0)
			meta = 1;
		if(l == 1)
			meta = 2;
		if(l == 2)
			meta = 0;
		if(l == 3)
			meta = 3;
		if((te = world.getTileEntity(pos)) == null)
			te = this.createNewTileEntity(world, meta);
		world.setTileEntity(pos, te);
		world.setBlockState(pos, this.getStateFromMeta(meta * 2));
	}
}