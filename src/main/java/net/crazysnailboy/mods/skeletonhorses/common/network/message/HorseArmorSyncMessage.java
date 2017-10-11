package net.crazysnailboy.mods.skeletonhorses.common.network.message;

import net.crazysnailboy.mods.skeletonhorses.capability.armor.CapabilityHorseArmorHandler;
import net.crazysnailboy.mods.skeletonhorses.capability.armor.IHorseArmorHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class HorseArmorSyncMessage implements IMessage, IMessageHandler<HorseArmorSyncMessage, IMessage>
{

	private int entityId;
	private ItemStack stack;


	public HorseArmorSyncMessage()
	{
	}

	public HorseArmorSyncMessage(AbstractHorse horse)
	{
		this.entityId = horse.getEntityId();
		if (horse.hasCapability(CapabilityHorseArmorHandler.HORSE_ARMOR_CAPABILITY, null))
		{
			IHorseArmorHandler capability = horse.getCapability(CapabilityHorseArmorHandler.HORSE_ARMOR_CAPABILITY, null);
			this.stack = capability.getStackInSlot(0);
		}
	}


	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityId = ByteBufUtils.readVarInt(buf, 4);
		this.stack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, this.entityId, 4);
		ByteBufUtils.writeItemStack(buf, this.stack);
	}


	@Override
	public IMessage onMessage(final HorseArmorSyncMessage message, MessageContext ctx)
	{
		final Minecraft minecraft = Minecraft.getMinecraft();
		final WorldClient world = minecraft.world;

		IThreadListener threadListener = (ctx.side == Side.SERVER ? (WorldServer)ctx.getServerHandler().player.world : Minecraft.getMinecraft());
		threadListener.addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				AbstractHorse horse = (AbstractHorse)world.getEntityByID(message.entityId);
				if (horse.hasCapability(CapabilityHorseArmorHandler.HORSE_ARMOR_CAPABILITY, null))
				{
					IHorseArmorHandler capability = horse.getCapability(CapabilityHorseArmorHandler.HORSE_ARMOR_CAPABILITY, null);
					capability.extractItem(0, 1, false);
					capability.insertItem(0, message.stack, false);
				}
			}
		});
		return null;
	}

}
