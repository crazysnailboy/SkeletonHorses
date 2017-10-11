package net.crazysnailboy.mods.skeletonhorses.capability.chest;

import net.crazysnailboy.mods.skeletonhorses.SkeletonHorsesMod;
import net.crazysnailboy.mods.skeletonhorses.common.network.message.HorseChestSyncMessage;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.ItemStackHandler;


public class HorseChestHandler extends ItemStackHandler implements IHorseChestHandler
{

	private AbstractHorse horse;

	private boolean hasChest;


	public HorseChestHandler()
	{
		super(15);
	}


	@Override
	public void setEntity(AbstractHorse horse)
	{
		this.horse = horse;
	}

	@Override
	public AbstractHorse getEntity()
	{
		return horse;
	}

	@Override
	public boolean hasChest()
	{
		return this.hasChest;
	}

	@Override
	public void setChested(boolean value)
	{
		this.hasChest = value;
		if (!horse.world.isRemote)
		{
			for (EntityPlayer player : ((WorldServer)horse.world).getEntityTracker().getTrackingPlayers(horse))
			{
				SkeletonHorsesMod.NETWORK.sendTo(new HorseChestSyncMessage(horse), (EntityPlayerMP)player);
			}
		}
	}


	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound compound = super.serializeNBT();
		compound.setBoolean("Chested", this.hasChest);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound compound)
	{
		super.deserializeNBT(compound);
		this.hasChest = compound.getBoolean("Chested");
	}

}
