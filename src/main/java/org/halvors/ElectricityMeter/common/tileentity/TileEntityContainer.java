package org.halvors.ElectricityMeter.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityContainer extends TileEntity implements IInventory {
	public ItemStack[] inventory;

	public TileEntityContainer() {
		inventory = new ItemStack[64];
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags) {
		super.readFromNBT(nbtTags);
		
		NBTTagList tagList = nbtTags.getTagList("Items", NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];

		for (int tagCount = 0; tagCount < tagList.tagCount(); tagCount++) {
			NBTTagCompound tagCompound = (NBTTagCompound) tagList.getCompoundTagAt(tagCount);
			byte slot = tagCompound.getByte("Slot");

			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tagCompound));
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags) {
		super.writeToNBT(nbtTags);

		NBTTagList tagList = new NBTTagList();

		for (int slotCount = 0; slotCount < getSizeInventory(); slotCount++) {
			if (getStackInSlot(slotCount) != null) {
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setByte("Slot", (byte) slotCount);
				getStackInSlot(slotCount).writeToNBT(tagCompound);
				tagList.appendTag(tagCompound);
			}
		}

		nbtTags.setTag("Items", tagList);
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (getStackInSlot(slot) != null) {
			ItemStack tempStack;

			if (getStackInSlot(slot).stackSize <= amount) {
				tempStack = getStackInSlot(slot);
				setInventorySlotContents(slot, null);

				return tempStack;
			} else {
				tempStack = getStackInSlot(slot).splitStack(amount);

				if (getStackInSlot(slot).stackSize == 0) {
					setInventorySlotContents(slot, null);
				}

				return tempStack;
			}
		}

		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (getStackInSlot(slot) != null) {
			ItemStack tempStack = getStackInSlot(slot);
			setInventorySlotContents(slot, null);

			return tempStack;
		}

		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		inventory[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
			itemStack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return !isInvalid();
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return true;
	}
}