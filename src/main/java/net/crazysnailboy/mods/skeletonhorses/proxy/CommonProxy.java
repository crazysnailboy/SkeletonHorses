package net.crazysnailboy.mods.skeletonhorses.proxy;

import net.crazysnailboy.mods.skeletonhorses.SkeletonHorsesMod;
import net.crazysnailboy.mods.skeletonhorses.capability.armor.CapabilityHorseArmorHandler;
import net.crazysnailboy.mods.skeletonhorses.capability.chest.CapabilityHorseChestHandler;
import net.crazysnailboy.mods.skeletonhorses.common.network.GuiHandler;
import net.crazysnailboy.mods.skeletonhorses.common.network.message.HorseArmorSyncMessage;
import net.crazysnailboy.mods.skeletonhorses.common.network.message.HorseChestSyncMessage;
import net.crazysnailboy.mods.skeletonhorses.common.network.message.HorseGuiOpenMessage;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy
{

	public void preInit()
	{
		this.registerNetworkMessages();
	}

	public void init()
	{
		this.registerCapabilities();
		this.registerGuiHandler();
	}

	public void postInit()
	{
	}


	private void registerCapabilities()
	{
		CapabilityHorseArmorHandler.register();
		CapabilityHorseChestHandler.register();
	}

	private void registerGuiHandler()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(SkeletonHorsesMod.INSTANCE, new GuiHandler());
	}

	private void registerNetworkMessages()
	{
		SkeletonHorsesMod.NETWORK.registerMessage(HorseArmorSyncMessage.class, HorseArmorSyncMessage.class, 0, Side.CLIENT);
		SkeletonHorsesMod.NETWORK.registerMessage(HorseChestSyncMessage.class, HorseChestSyncMessage.class, 1, Side.CLIENT);
		SkeletonHorsesMod.NETWORK.registerMessage(HorseGuiOpenMessage.class, HorseGuiOpenMessage.class, 2, Side.SERVER);
	}

}
