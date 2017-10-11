package net.crazysnailboy.mods.skeletonhorses.entity;

import java.lang.reflect.Method;
import net.crazysnailboy.mods.skeletonhorses.SkeletonHorsesMod;
import net.crazysnailboy.mods.skeletonhorses.capability.chest.CapabilityHorseChestHandler;
import net.crazysnailboy.mods.skeletonhorses.capability.chest.IHorseChestHandler;
import net.crazysnailboy.mods.skeletonhorses.common.network.GuiHandler;
import net.crazysnailboy.mods.skeletonhorses.util.ReflectionHelper;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;


public class EntitySkeletonHorseHooks
{

	public static boolean processInitialInteract(EntitySkeletonHorse horse, EntityPlayer player, EnumHand hand)
	{
		System.out.println("processInitialInteract :: { isRemote=" + player.world.isRemote + ", hand=" + hand + ",isLeashed=" + horse.getLeashed() + " }");

		if (horse.getLeashed() && horse.getLeashedToEntity() == player) // && hand == EnumHand.MAIN_HAND)
		{
			horse.clearLeashed(true, !player.capabilities.isCreativeMode);
			return true;
		}
		else
		{
			ItemStack itemstack = player.getHeldItem(hand);

			if (itemstack.getItem() == Items.LEAD && !horse.getLeashed()) // && this.canBeLeashedTo(player))
			{
				horse.setLeashedToEntity(player, true);
				if (!player.capabilities.isCreativeMode)
				{
					itemstack.shrink(1);
				}

				System.out.println("processInitialInteract :: { hand=" + hand + ",isLeashed=" + horse.getLeashed() + " }");

				return true;
			}
			else
			{
				return processInteract(horse, player, hand); // ? true : super.processInitialInteract(player, hand);
			}
		}
	}


	public static boolean processInteract(EntitySkeletonHorse horse, EntityPlayer player, EnumHand hand)
	{
		AbstractHorse abstractHorse = (AbstractHorse)horse;

		ItemStack itemstack = player.getHeldItem(hand);
		boolean haveStack = !itemstack.isEmpty();

		if (haveStack && itemstack.getItem() == Items.SPAWN_EGG)
		{
			return abstractHorse.processInteract(player, hand);
		}
		else
		{
			if (!horse.isChild())
			{
				if (horse.isTame() && player.isSneaking())
				{
					player.openGui(SkeletonHorsesMod.INSTANCE, GuiHandler.GUI_HORSE_INVENTORY, player.world, horse.getEntityId(), -1, -1);
//					horse.openGUI(player);
					return true;
				}

				if (horse.isBeingRidden())
				{
					return abstractHorse.processInteract(player, hand);
				}
			}

			if (haveStack)
			{
				if (handleEating(abstractHorse, player, itemstack))
				{
					if (!player.capabilities.isCreativeMode)
					{
						itemstack.shrink(1);
					}
					return true;
				}

				if (horse.hasCapability(CapabilityHorseChestHandler.HORSE_CHEST_CAPABILITY, null))
				{
					IHorseChestHandler capability = horse.getCapability(CapabilityHorseChestHandler.HORSE_CHEST_CAPABILITY, null);

					if (!capability.hasChest() && itemstack.getItem() == Item.getItemFromBlock(Blocks.CHEST))
					{
						capability.setChested(true);
						playChestEquipSound(horse);
					}

				}


				if (itemstack.interactWithEntity(player, horse, hand))
				{
					return true;
				}

				if (!horse.isTame())
				{
					horse.makeMad();
					return true;
				}

				boolean flag1 = HorseArmorType.getByItemStack(itemstack) != HorseArmorType.NONE;
				boolean flag2 = !horse.isChild() && !horse.isHorseSaddled() && itemstack.getItem() == Items.SADDLE;

				if (flag1 || flag2)
				{
					player.openGui(SkeletonHorsesMod.INSTANCE, GuiHandler.GUI_HORSE_INVENTORY, player.world, horse.getEntityId(), -1, -1);
//					horse.openGUI(player);
					return true;
				}
			}

			if (horse.isChild())
			{
				return abstractHorse.processInteract(player, hand);
			}
			else
			{
				mountTo(abstractHorse, player);
				return true;
			}
		}
	}

	private static boolean handleEating(AbstractHorse horse, EntityPlayer player, ItemStack stack)
	{
		return ReflectionHelper.invokeMethod(handleEatingMethod, horse, player, stack);
	}

	private static void mountTo(AbstractHorse horse, EntityPlayer player)
	{
		ReflectionHelper.invokeMethod(mountToMethod, horse, player);
	}


	private static final Method handleEatingMethod = ReflectionHelper.getDeclaredMethod(AbstractHorse.class, new String[] { "handleEating", "func_190678_b" }, EntityPlayer.class, ItemStack.class);
	private static final Method mountToMethod = ReflectionHelper.getDeclaredMethod(AbstractHorse.class, new String[] { "mountTo", "func_110237_h" }, EntityPlayer.class);


    private static void playChestEquipSound(AbstractHorse horse)
    {
    	horse.playSound(SoundEvents.ENTITY_DONKEY_CHEST, 1.0F, (horse.getRNG().nextFloat() - horse.getRNG().nextFloat()) * 0.2F + 1.0F);
    }

}
