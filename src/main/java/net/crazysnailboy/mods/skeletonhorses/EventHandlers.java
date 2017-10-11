package net.crazysnailboy.mods.skeletonhorses;

import net.crazysnailboy.mods.skeletonhorses.capability.chest.CapabilityHorseChestHandler;
import net.crazysnailboy.mods.skeletonhorses.capability.chest.IHorseChestHandler;
import net.crazysnailboy.mods.skeletonhorses.common.network.message.HorseArmorSyncMessage;
import net.crazysnailboy.mods.skeletonhorses.common.network.message.HorseChestSyncMessage;
import net.crazysnailboy.mods.skeletonhorses.entity.EntitySkeletonHorseHooks;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@EventBusSubscriber
public class EventHandlers
{

	@SubscribeEvent
	public static void onPlayerEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (event.getTarget() instanceof EntitySkeletonHorse)
		{
			if (!event.getWorld().isRemote && event.getHand() == EnumHand.MAIN_HAND)
			{
				EntitySkeletonHorse skeletonHorse = (EntitySkeletonHorse)event.getTarget();
				EntitySkeletonHorseHooks.processInitialInteract(skeletonHorse, event.getEntityPlayer(), event.getHand());
			}
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerTrackingEntity(PlayerEvent.StartTracking event)
	{
		if (event.getTarget() instanceof EntitySkeletonHorse)
		{
			AbstractHorse horse = (AbstractHorse)event.getTarget();
			SkeletonHorsesMod.NETWORK.sendTo(new HorseArmorSyncMessage(horse), (EntityPlayerMP)event.getEntityPlayer());
			SkeletonHorsesMod.NETWORK.sendTo(new HorseChestSyncMessage(horse), (EntityPlayerMP)event.getEntityPlayer());
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event)
	{
		if (event.getEntityLiving() instanceof EntitySkeletonHorse && !event.getEntityLiving().world.isRemote)
		{
			AbstractHorse horse = (AbstractHorse)event.getEntityLiving();
			if (horse.hasCapability(CapabilityHorseChestHandler.HORSE_CHEST_CAPABILITY, null))
			{
				IHorseChestHandler capability = horse.getCapability(CapabilityHorseChestHandler.HORSE_CHEST_CAPABILITY, null);
				if (capability.hasChest())
				{
					for ( int i = 0 ; i <  capability.getSlots() ; i++ )
					{
						ItemStack stack = capability.getStackInSlot(i);
						if (stack != ItemStack.EMPTY)
						{
							event.getEntityLiving().entityDropItem(stack, 0.0F);
						}
					}
	                horse.dropItem(Item.getItemFromBlock(Blocks.CHEST), 1);
	                capability.setChested(false);
				}
			}
		}
	}

}
