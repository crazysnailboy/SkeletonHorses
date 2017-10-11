package net.crazysnailboy.mods.skeletonhorses.capability.chest;

import net.crazysnailboy.mods.skeletonhorses.capability.IEntityAware;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;


public interface IHorseChestHandler extends IItemHandler, IEntityAware<AbstractHorse>, INBTSerializable<NBTTagCompound>
{

	boolean hasChest();

	void setChested(boolean value);

}
