package austeretony.better_merchants.common.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import austeretony.better_merchants.common.util.ItemStackWrapper;
import austeretony.better_merchants.common.util.MathUtils;
import austeretony.better_merchants.common.util.StreamUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class MerchantOffer {

    public final long offerId;

    private ItemStackWrapper offeredStack;

    private int 
    amount, 
    buyCost,//if buying from merchant 
    sellingCost;//if selling to merchant

    private boolean sellingEnabled, sellingOnly;

    public MerchantOffer(long offerId, ItemStackWrapper offeredStack) {
        this.offerId = offerId;
        this.offeredStack = offeredStack;
    }

    public void setItemStack(ItemStackWrapper offeredStack) {
        this.offeredStack = offeredStack;
    }

    public ItemStackWrapper getOfferedStack() {
        return this.offeredStack;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public boolean isSellingEnabled() {
        return this.sellingEnabled;
    }

    public void setSellingEnabled(boolean flag) {
        this.sellingEnabled = flag;
    }

    public boolean isSellingOnly() {
        return this.sellingOnly;
    }

    public void setSellingOnly(boolean flag) {
        this.sellingOnly = flag;
    }

    public int getBuyCost() {
        return this.buyCost;
    }

    public void setBuyCost(int value) {
        this.buyCost = MathUtils.clamp(value, 0, Integer.MAX_VALUE);
    }

    public int getSellingCost() {
        return this.sellingCost;
    }

    public void setSellingCost(int value) {
        this.sellingCost = MathUtils.clamp(value, 0, Integer.MAX_VALUE);
    }

    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.offerId, bos);
        this.offeredStack.write(bos);
        StreamUtils.write((short) this.amount, bos);
        StreamUtils.write(this.sellingEnabled, bos);
        StreamUtils.write(this.sellingOnly, bos);
        StreamUtils.write(this.buyCost, bos);
        StreamUtils.write(this.sellingCost, bos);
    }

    public static MerchantOffer read(BufferedInputStream bis) throws IOException {
        MerchantOffer offer = new MerchantOffer(StreamUtils.readLong(bis), ItemStackWrapper.read(bis));
        offer.amount = StreamUtils.readShort(bis);
        offer.sellingEnabled = StreamUtils.readBoolean(bis);
        offer.sellingOnly = StreamUtils.readBoolean(bis);
        offer.buyCost = StreamUtils.readInt(bis);
        offer.sellingCost = StreamUtils.readInt(bis);
        return offer;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeLong(this.offerId);
        this.offeredStack.write(buffer);
        buffer.writeShort((short) this.amount);
        buffer.writeBoolean(this.sellingEnabled);
        buffer.writeBoolean(this.sellingOnly);
        buffer.writeInt(this.buyCost);
        buffer.writeInt(this.sellingCost);
    }

    public static MerchantOffer read(PacketBuffer buffer) {
        MerchantOffer offer = new MerchantOffer(buffer.readLong(), ItemStackWrapper.read(buffer));
        offer.amount = buffer.readShort();
        offer.sellingEnabled = buffer.readBoolean();      
        offer.sellingOnly = buffer.readBoolean();      
        offer.buyCost = buffer.readInt();
        offer.sellingCost = buffer.readInt();
        return offer;
    }

    public NBTTagCompound toTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("id", this.offerId);
        this.offeredStack.write(tag);
        tag.setShort("amount", (short) this.amount);
        tag.setBoolean("sell_e", this.sellingEnabled);
        tag.setBoolean("sell_o", this.sellingOnly);
        tag.setInteger("buy_c", this.buyCost);
        tag.setInteger("sell_c", this.sellingCost);
        return tag;
    }

    public static MerchantOffer fromTagCompound(NBTTagCompound tag) {
        MerchantOffer offer = new MerchantOffer(tag.getLong("id"), ItemStackWrapper.read(tag));
        offer.amount = tag.getShort("amount");
        offer.sellingEnabled = tag.getBoolean("sell_e");      
        offer.sellingOnly = tag.getBoolean("sell_o");      
        offer.buyCost = tag.getInteger("buy_c");
        offer.sellingCost = tag.getInteger("sell_c");
        return offer;
    }

    public MerchantOffer copy() {
        MerchantOffer offer = new MerchantOffer(this.offerId, this.offeredStack.copy());
        offer.amount = this.amount;
        offer.sellingEnabled = this.sellingEnabled;    
        offer.sellingOnly = this.sellingOnly;      
        offer.buyCost = this.buyCost;
        offer.sellingCost = this.sellingCost;
        return offer;
    }
}