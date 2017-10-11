package net.crazysnailboy.mods.skeletonhorses.capability.armor;

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
public class CapabilityHorseArmorHandler
{

	@CapabilityInject(IHorseArmorHandler.class)
	public static final Capability<IHorseArmorHandler> HORSE_ARMOR_CAPABILITY = null;


	public static void register()
	{
		CapabilityManager.INSTANCE.register(IHorseArmorHandler.class, new Capabilities.Storage<>(), () -> new HorseArmorHandler());
	}


	@SubscribeEvent
	public static void onAttachCapability(final AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof EntitySkeletonHorse)
		{
			AbstractHorse horse = (AbstractHorse)event.getObject();
			event.addCapability(new ResourceLocation(SkeletonHorsesMod.MODID, "HorseArmor"), new EntityAwareProvider(HORSE_ARMOR_CAPABILITY, null, horse));
		}
	}

}
