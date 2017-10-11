package net.crazysnailboy.mods.skeletonhorses.capability.armor;

import net.crazysnailboy.mods.skeletonhorses.capability.IEntityAware;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;


public interface IHorseArmorHandler extends IItemHandler, IEntityAware<AbstractHorse>, INBTSerializable<NBTTagCompound>
{

	@SideOnly(Side.CLIENT)
	String getHorseTexture();

	@SideOnly(Side.CLIENT)
	String[] getVariantTexturePaths();

}
