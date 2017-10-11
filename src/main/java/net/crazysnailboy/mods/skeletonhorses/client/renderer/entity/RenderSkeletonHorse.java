package net.crazysnailboy.mods.skeletonhorses.client.renderer.entity;

import java.util.Map;
import net.crazysnailboy.mods.skeletonhorses.SkeletonHorsesMod;
import net.crazysnailboy.mods.skeletonhorses.capability.armor.CapabilityHorseArmorHandler;
import net.crazysnailboy.mods.skeletonhorses.capability.armor.IHorseArmorHandler;
import net.crazysnailboy.mods.skeletonhorses.client.model.ModelHorse;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.util.ResourceLocation;


public class RenderSkeletonHorse extends RenderLiving<EntitySkeletonHorse>
{

	private static final ResourceLocation ENTITY_TEXTURE = new ResourceLocation("textures/entity/horse/horse_skeleton.png");
	private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.<String, ResourceLocation>newHashMap();


	public RenderSkeletonHorse(RenderManager renderManager)
	{
		super(renderManager, new ModelHorse(), 0.75F);
	}


	@Override
	protected ResourceLocation getEntityTexture(EntitySkeletonHorse entity)
	{
		if (entity.hasCapability(CapabilityHorseArmorHandler.HORSE_ARMOR_CAPABILITY, null))
		{
			try
			{
				IHorseArmorHandler capability = entity.getCapability(CapabilityHorseArmorHandler.HORSE_ARMOR_CAPABILITY, null);

		        String s = capability.getHorseTexture();

				ResourceLocation resourcelocation = LAYERED_LOCATION_CACHE.get(s);

				if (resourcelocation == null)
				{
					resourcelocation = new ResourceLocation(s);
					Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, new LayeredTexture(capability.getVariantTexturePaths()));
					LAYERED_LOCATION_CACHE.put(s, resourcelocation);
				}

		        return resourcelocation;
			}
			catch(Exception ex)
			{
				SkeletonHorsesMod.LOGGER.catching(ex);
				return ENTITY_TEXTURE;
			}
		}
		return ENTITY_TEXTURE;
	}

}
