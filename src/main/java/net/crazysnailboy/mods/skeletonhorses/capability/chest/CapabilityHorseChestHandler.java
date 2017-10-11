package net.crazysnailboy.mods.skeletonhorses.capability.chest;

import net.crazysnailboy.mods.skeletonhorses.SkeletonHorsesMod;
import net.crazysnailboy.mods.skeletonhorses.capability.Capabilities;
import net.crazysnailboy.mods.skeletonhorses.capability.Capabilities.EntityAwareProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@EventBusSubscriber
public class CapabilityHorseChestHandler
{

	@CapabilityInject(IHorseChestHandler.class)
	public static final Capability<IHorseChestHandler> HORSE_CHEST_CAPABILITY = null;


	public static void register()
	{
		CapabilityManager.INSTANCE.register(IHorseChestHandler.class, new Capabilities.Storage<>(), () -> new HorseChestHandler());
	}

	@SubscribeEvent
	public static void onAttachCapability(final AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof EntitySkeletonHorse)
		{
			AbstractHorse horse = (AbstractHorse)event.getObject();
			event.addCapability(new ResourceLocation(SkeletonHorsesMod.MODID, "HorseChest"), new EntityAwareProvider(HORSE_CHEST_CAPABILITY, null, horse));
		}
	}

}
