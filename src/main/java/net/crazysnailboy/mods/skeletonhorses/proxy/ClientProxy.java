package net.crazysnailboy.mods.skeletonhorses.proxy;

import net.crazysnailboy.mods.skeletonhorses.init.ModEntities;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
		super.preInit();
		this.registerEntityRenderers();
	}

	@Override
	public void init()
	{
		super.init();
	}

	@Override
	public void postInit()
	{
		super.postInit();
	}


	private void registerEntityRenderers()
	{
		ModEntities.registerRenderingHandlers();
	}

}
