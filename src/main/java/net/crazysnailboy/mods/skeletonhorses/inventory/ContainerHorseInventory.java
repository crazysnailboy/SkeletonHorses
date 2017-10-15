package net.crazysnailboy.mods.skeletonhorses.inventory;

import net.crazysnailboy.mods.skeletonhorses.capability.armor.CapabilityHorseArmorHandler;
import net.crazysnailboy.mods.skeletonhorses.capability.armor.HorseArmorHandler;
import net.crazysnailboy.mods.skeletonhorses.capability.armor.IHorseArmorHandler;
import net.crazysnailboy.mods.skeletonhorses.capability.chest.CapabilityHorseChestHandler;
import net.crazysnailboy.mods.skeletonhorses.capability.chest.IHorseChestHandler;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;


public class ContainerHorseInventory extends Container
{

	private final IInventory horseInventory;
	private final AbstractHorse horse;

	private int inventorySize = 1;


	public ContainerHorseInventory(IInventory playerInventory, IInventory horseInventory, final AbstractHorse horse, EntityPlayer player)
	{
		this.horseInventory = horseInventory;
		this.horse = horse;
		int i = 3;
		horseInventory.openInventory(player);
		int j = -18;
		this.addSlotToContainer(new Slot(horseInventory, 0, 8, 18)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() == Items.SADDLE && !this.getHasStack() && horse.canBeSaddled();
			}

			@Override
			@SideOnly(Side.CLIENT)
			public boolean isEnabled()
			{
				return horse.canBeSaddled();
			}
		});

		if (horse.hasCapability(CapabilityHorseArmorHandler.HORSE_ARMOR_CAPABILITY, null))
		{
			IHorseArmorHandler capability = horse.getCapability(CapabilityHorseArmorHandler.HORSE_ARMOR_CAPABILITY, null);
			this.addSlotToContainer(new SlotItemHandler(capability, 0, 8, 36)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return HorseArmorType.isHorseArmor(stack.getItem());
				}

				@Override
				public void onSlotChanged()
				{
			    	((HorseArmorHandler)this.getItemHandler()).onContentsChanged(this.getSlotIndex());
				}
			});

			inventorySize = inventorySize + 1;
		}



//		this.addSlotToContainer(new Slot(horseInventory, 1, 8, 36)
//		{
//			@Override
//			public boolean isItemValid(ItemStack stack)
//			{
//				return HorseArmorType.isHorseArmor(stack.getItem());
//			}
//
//			@Override
//			public int getSlotStackLimit()
//			{
//				return 1;
//			}
//
//			@Override
//			@SideOnly(Side.CLIENT)
//			public boolean isEnabled()
//			{
//				return true;
//			}
//		});

		if (horse.hasCapability(CapabilityHorseChestHandler.HORSE_CHEST_CAPABILITY, null))
		{
			IHorseChestHandler capability = horse.getCapability(CapabilityHorseChestHandler.HORSE_CHEST_CAPABILITY, null);
			if (capability.hasChest())
			{
				for (int k = 0; k < 3; ++k)
				{
					for (int l = 0; l < 5; ++l)
					{
						this.addSlotToContainer(new SlotItemHandler(capability, l + k * 5, 80 + l * 18, 18 + k * 18));
					}
				}
				inventorySize = inventorySize + 15;
			}
		}

//		if (horse instanceof AbstractChestHorse && ((AbstractChestHorse)horse).hasChest())
//		{
//			for (int k = 0; k < 3; ++k)
//			{
//				for (int l = 0; l < ((AbstractChestHorse)horse).getInventoryColumns(); ++l)
//				{
//					this.addSlotToContainer(new Slot(horseInventory, 2 + l + k * ((AbstractChestHorse)horse).getInventoryColumns(), 80 + l * 18, 18 + k * 18));
//				}
//			}
//		}

		for (int i1 = 0; i1 < 3; ++i1)
		{
			for (int k1 = 0; k1 < 9; ++k1)
			{
				this.addSlotToContainer(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
			}
		}

		for (int j1 = 0; j1 < 9; ++j1)
		{
			this.addSlotToContainer(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return this.horseInventory.isUsableByPlayer(player) && this.horse.isEntityAlive() && this.horse.getDistanceToEntity(player) < 8.0F;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < inventorySize)
			{
				if (!this.mergeItemStack(itemstack1, inventorySize, this.inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack())
			{
				if (!this.mergeItemStack(itemstack1, 1, 2, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (this.getSlot(0).isItemValid(itemstack1))
			{
				if (!this.mergeItemStack(itemstack1, 0, 1, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (inventorySize <= 2 || !this.mergeItemStack(itemstack1, 2, inventorySize, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		this.horseInventory.closeInventory(player);
	}

}