package net.crazysnailboy.mods.skeletonhorses.capability;

import net.minecraft.entity.Entity;


public interface IEntityAware<T extends Entity>
{

	void setEntity(T entity);

	T getEntity();

}
