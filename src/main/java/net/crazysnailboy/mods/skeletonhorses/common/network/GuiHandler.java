package net.crazysnailboy.mods.skeletonhorses.common.network;

import java.lang.reflect.Field;
import net.crazysnailboy.mods.skeletonhorses.client.gui.inventory.GuiScreenHorseInventory;
import net.crazysnailboy.mods.skeletonhorses.inventory.ContainerHorseInventory;
import net.crazysnailboy.mods.skeletonhorses.util.ReflectionHelper;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;


public class GuiHandler implements IGuiHandler
{

	private static final Field horseChestField = ReflectionHelper.getDeclaredField(AbstractHorse.class, "horseChest", "field_110296_bG");

	public static final int GUI_HORSE_INVENTORY = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int entityId, int unused1, int unused2)
	{
		if (ID == GUI_HORSE_INVENTORY)
		{
			AbstractHorse horse = (AbstractHorse)world.getEntityByID(entityId);
			if (horse != null)
			{
				ContainerHorseChest horseInventory = ReflectionHelper.getFieldValue(horseChestField, horse);
				return new ContainerHorseInventory(player.inventory, horseInventory, horse, player);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int entityId, int unused1, int unused2)
	{
		if (ID == GUI_HORSE_INVENTORY)
		{
			AbstractHorse horse = (AbstractHorse)world.getEntityByID(entityId);
			if (horse != null)
			{
				ContainerHorseChest horseInventory = ReflectionHelper.getFieldValue(horseChestField, horse);
				return new GuiScreenHorseInventory(player.inventory, horseInventory, horse);
			}
		}
		return null;
	}

}
