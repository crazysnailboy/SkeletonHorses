package net.crazysnailboy.mods.skeletonhorses.common.network.message;

import net.crazysnailboy.mods.skeletonhorses.capability.chest.CapabilityHorseChestHandler;
import net.crazysnailboy.mods.skeletonhorses.capability.chest.IHorseChestHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;


public class HorseChestSyncMessage implements IMessage, IMessageHandler<HorseChestSyncMessage, IMessage>
{

	private int entityId;
	private boolean hasChest;


	public HorseChestSyncMessage()
	{
	}

	public HorseChestSyncMessage(AbstractHorse horse)
	{
		this.entityId = horse.getEntityId();
		if (horse.hasCapability(CapabilityHorseChestHandler.HORSE_CHEST_CAPABILITY, null))
		{
			IHorseChestHandler capability = horse.getCapability(CapabilityHorseChestHandler.HORSE_CHEST_CAPABILITY, null);
			this.hasChest = capability.hasChest();
		}
	}


	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityId = ByteBufUtils.readVarInt(buf, 4);
		this.hasChest = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, this.entityId, 4);
		buf.writeBoolean(this.hasChest);
	}


	@Override
	public IMessage onMessage(final HorseChestSyncMessage message, MessageContext ctx)
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
				if (horse.hasCapability(CapabilityHorseChestHandler.HORSE_CHEST_CAPABILITY, null))
				{
					IHorseChestHandler capability = horse.getCapability(CapabilityHorseChestHandler.HORSE_CHEST_CAPABILITY, null);
					capability.setChested(message.hasChest);
				}
			}
		});
		return null;
	}

}
