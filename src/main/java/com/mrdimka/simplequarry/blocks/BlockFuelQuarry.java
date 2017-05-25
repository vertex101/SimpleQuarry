package com.mrdimka.simplequarry.blocks;

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

import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.EnumRotation;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.gui.GuiManager;
import com.mrdimka.hammercore.tile.TileSyncable;
import com.mrdimka.simplequarry.tile.TileFuelQuarry;

public class BlockFuelQuarry extends Block implements ITileEntityProvider, ITileBlock<TileFuelQuarry>
{
	public static final PropertyBool IS_MINING = PropertyBool.create("mining");
	
	public BlockFuelQuarry()
	{
		super(Material.ROCK);
		setSoundType(SoundType.STONE);
		setUnlocalizedName("fuel_quarry");
		setHardness(4F);
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
		w.setBlockState(p, getStateFromMeta(meta));
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
	
	public IBlockState getStateFromMeta(int meta)
	{
		int meta0 = (int) Math.floor(meta / 2D);
		int meta1 = meta % 2;
		
		return getDefaultState().withProperty(EnumRotation.FACING, EnumRotation.values()[meta0 % EnumRotation.values().length]).withProperty(IS_MINING, true);
	}
	
	public int getMetaFromState(IBlockState state)
	{
		int meta0 = ((EnumRotation) state.getValue(EnumRotation.FACING)).ordinal();
		boolean meta1 = state.getValue(IS_MINING);
		
		return meta0 == 0 && meta1 ? 0 : meta0 == 0 && !meta1 ? 1 : meta0 == 1 && meta1 ? 2 : meta0 == 1 && !meta1 ? 3 : meta0 == 2 && meta1 ? 4 : meta0 == 2 && !meta1 ? 5 : meta0 == 3 && meta1 ? 6 : meta0 == 3 && !meta1 ? 7 : 0;
	}
	
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, getProperties());
	}
	
	public IProperty[] getProperties()
	{
		return new IProperty[] { EnumRotation.FACING, IS_MINING };
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase ent, ItemStack stack)
	{
		int l = MathHelper.floor((double) (ent.rotationYaw * 4F / 360F) + 0.5D) & 3;
		int meta = 0;
		if(l == 0)
			meta = 1;
		if(l == 1)
			meta = 2;
		if(l == 2)
			meta = 0;
		if(l == 3)
			meta = 3;
		
		TileEntity te = world.getTileEntity(pos);
		if(te == null)
			te = createNewTileEntity(world, meta);
		world.setTileEntity(pos, te);
		
		world.setBlockState(pos, getStateFromMeta(meta * 2));
	}
	
	@Override
	public void breakBlock(World w, BlockPos p, IBlockState s)
	{
		TileEntity t = w.getTileEntity(p);
		
		if(t instanceof TileFuelQuarry)
		{
			TileFuelQuarry f = (TileFuelQuarry) t;
			f.inv.drop(w, p);
			f.invTemp.drop(w, p);
		}
		
		super.breakBlock(w, p, s);
	}
}