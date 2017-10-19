package net.crazysnailboy.mods.skeletonhorses.common.network.message;

import io.netty.buffer.ByteBuf;
import net.crazysnailboy.mods.skeletonhorses.SkeletonHorsesMod;
import net.crazysnailboy.mods.skeletonhorses.common.network.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;


public class HorseGuiOpenMessage implements IMessage, IMessageHandler<HorseGuiOpenMessage, IMessage>
{

	private int entityId;

	public HorseGuiOpenMessage()
	{
	}

	public HorseGuiOpenMessage(AbstractHorse horse)
	{
		this.entityId = horse.getEntityId();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityId = ByteBufUtils.readVarInt(buf, 4);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, this.entityId, 4);
	}

	@Override
	public IMessage onMessage(final HorseGuiOpenMessage message, MessageContext ctx)
	{
		IThreadListener threadListener = (ctx.side == Side.SERVER ? (WorldServer)ctx.getServerHandler().player.world : Minecraft.getMinecraft());
		threadListener.addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				EntityPlayerMP player = ctx.getServerHandler().player;
				player.openGui(SkeletonHorsesMod.INSTANCE, GuiHandler.GUI_HORSE_INVENTORY, player.world, message.entityId, -1, -1);
			}
		});
		return null;
	}

}
