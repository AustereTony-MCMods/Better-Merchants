package austeretony.better_merchants.common.capability;

import austeretony.better_merchants.common.main.EntityEntry;
import austeretony.better_merchants.common.main.MerchantProfile;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MerchantStorage implements IStorage<IMerchant> {

    @Override
    public NBTBase writeNBT(Capability<IMerchant> capability, IMerchant instance, EnumFacing side) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("available", instance.available());
        if (instance.available()) {
            tag.setTag("entry", instance.getEntityEntry().toTagCompound());
            tag.setTag("profile", instance.getMerchantProfile().toTagCompound());
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<IMerchant> capability, IMerchant instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        if (tag.getBoolean("available")) {
            instance.setAvailable();
            instance.setEntityEntry(EntityEntry.fromTagCompound(tag.getCompoundTag("entry")));
            instance.setMerchantProfile(MerchantProfile.fromTagCompound(tag.getCompoundTag("profile")));
        }
    }       
}