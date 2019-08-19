package austeretony.better_merchants.common.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class MerchantProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IMerchant.class)
    public static final Capability<IMerchant> MERCHANT = null;

    private IMerchant instance = MERCHANT.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == MERCHANT;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == MERCHANT ? MERCHANT.<T> cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return MERCHANT.getStorage().writeNBT(MERCHANT, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        MERCHANT.getStorage().readNBT(MERCHANT, this.instance, null, nbt);
    }
}