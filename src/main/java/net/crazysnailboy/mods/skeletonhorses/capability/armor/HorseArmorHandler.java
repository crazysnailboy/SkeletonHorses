package net.crazysnailboy.mods.skeletonhorses.capability.armor;

import java.util.UUID;
import net.crazysnailboy.mods.skeletonhorses.SkeletonHorsesMod;
import net.crazysnailboy.mods.skeletonhorses.common.network.message.HorseArmorSyncMessage;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;


public class HorseArmorHandler extends ItemStackHandler implements IHorseArmorHandler
{

	private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

	private AbstractHorse horse;

	private String texturePrefix;
	private final String[] horseTexturesArray = new String[3];


	@Override
	protected void onContentsChanged(int slot)
	{
		this.setHorseArmorStack(this.getStackInSlot(slot));
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


	private void setHorseArmorStack(ItemStack stack)
	{
		HorseArmorType horsearmortype = HorseArmorType.getByItemStack(stack);
        this.resetTexturePrefix();

		if (!horse.world.isRemote)
		{
			horse.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
			int i = horsearmortype.getProtection();

			if (i != 0)
			{
				horse.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier((new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", (double)i, 0)).setSaved(false));
			}

			for (EntityPlayer player : ((WorldServer)horse.world).getEntityTracker().getTrackingPlayers(horse))
			{
				SkeletonHorsesMod.NETWORK.sendTo(new HorseArmorSyncMessage(horse), (EntityPlayerMP)player);
			}
		}
	}

	private void resetTexturePrefix()
    {
        this.texturePrefix = null;
    }

    @SideOnly(Side.CLIENT)
    private void setHorseTexturePaths()
	{
        HorseArmorType horsearmortype = HorseArmorType.getByItemStack(this.getStackInSlot(0));
		this.horseTexturesArray[0] = "textures/entity/horse/horse_skeleton.png";
		this.horseTexturesArray[1] = null;
		this.horseTexturesArray[2] = horsearmortype.getTextureName();
		this.texturePrefix = "horse/" + horsearmortype.getHash();
	}

    @Override
	@SideOnly(Side.CLIENT)
    public String getHorseTexture()
    {
        if (this.texturePrefix == null)
        {
            this.setHorseTexturePaths();
        }
        return this.texturePrefix;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public String[] getVariantTexturePaths()
    {
        if (this.texturePrefix == null)
        {
            this.setHorseTexturePaths();
        }
        return this.horseTexturesArray;
    }

}
