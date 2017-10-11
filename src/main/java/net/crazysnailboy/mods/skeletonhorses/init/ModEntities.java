package net.crazysnailboy.mods.skeletonhorses.init;

import net.crazysnailboy.mods.skeletonhorses.client.renderer.entity.RenderSkeletonHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities
{

	@SideOnly(Side.CLIENT)
	public static void registerRenderingHandlers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonHorse.class, RenderSkeletonHorse::new);
	}

}
