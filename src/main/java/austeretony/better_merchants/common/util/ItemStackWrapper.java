package austeretony.better_merchants.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import austeretony.better_merchants.common.main.BetterMerchantsMain;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class ItemStackWrapper {

    public final int itemId, meta, damage;

    public final String nbtStr;//stored as json string

    public ItemStackWrapper(int itemId, int meta, int damage, String nbtStr) {
        this.itemId = itemId;
        this.meta = meta;   
        this.damage = damage;   
        this.nbtStr = nbtStr;
    }

    public static ItemStackWrapper getFromStack(ItemStack itemStack) {
        String nbtStr = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "";
        return new ItemStackWrapper(Item.getIdFromItem(itemStack.getItem()), itemStack.getMetadata(), itemStack.getItemDamage(), nbtStr);
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Item.getItemById(this.itemId), 1, this.meta);
        itemStack.setItemDamage(this.damage);
        if (!this.nbtStr.isEmpty()) {
            try {
                itemStack.setTagCompound(JsonToNBT.getTagFromJson(this.nbtStr));
            } catch (NBTException exception) {
                BetterMerchantsMain.LOGGER.error("ItemStack {} NBT parsing failure!", itemStack.toString());
                exception.printStackTrace();    
            }
        }
        return itemStack;
    }

    public boolean isEquals(ItemStack itemStack) {
        return InventoryHelper.isEquals(itemStack, this.itemId, this.meta, this.damage, this.nbtStr);     
    }

    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.itemId, bos);
        StreamUtils.write((byte) this.meta, bos);
        StreamUtils.write((short) this.damage, bos);
        StreamUtils.write(this.nbtStr, bos);
    }

    public static ItemStackWrapper read(BufferedInputStream bis) throws IOException {
        return new ItemStackWrapper(StreamUtils.readInt(bis), StreamUtils.readByte(bis), StreamUtils.readShort(bis), StreamUtils.readString(bis));
    }

    public void write(PacketBuffer buffer) {
        buffer.writeInt(this.itemId);
        buffer.writeByte(this.meta);
        buffer.writeShort(this.damage);
        PacketBufferUtils.writeString(this.nbtStr, buffer);
    }

    public static ItemStackWrapper read(PacketBuffer buffer) {
        return new ItemStackWrapper(buffer.readInt(), buffer.readByte(), buffer.readShort(), PacketBufferUtils.readString(buffer));
    }

    public void write(NBTTagCompound tag) {
        tag.setInteger("item_id", this.itemId);
        tag.setByte("meta", (byte) this.meta);
        tag.setShort("damage", (short) this.damage);
        tag.setString("nbt", this.nbtStr);
    }

    public static ItemStackWrapper read(NBTTagCompound tag) {
        return new ItemStackWrapper(tag.getInteger("item_id"), tag.getByte("meta"), tag.getShort("damage"), tag.getString("nbt"));
    }

    public ItemStackWrapper copy() {
        return new ItemStackWrapper(this.itemId, this.meta, this.damage, this.nbtStr);
    }
}
