package austeretony.better_merchants.common.capability;

import austeretony.better_merchants.common.main.EntityEntry;
import austeretony.better_merchants.common.main.MerchantProfile;

public class Merchant implements IMerchant {

    private boolean available;

    private EntityEntry entry;

    private MerchantProfile profile;

    @Override
    public boolean available() {
        return this.available;
    }

    @Override
    public void setAvailable() {
        this.available = true;
    }

    @Override
    public EntityEntry getEntityEntry() {
        return this.entry;
    }

    @Override
    public void setEntityEntry(EntityEntry entry) {
        this.entry = entry;
    }

    @Override
    public MerchantProfile getMerchantProfile() {
        return this.profile;
    }

    @Override
    public void setMerchantProfile(MerchantProfile profile) {
        this.profile = profile;
    }
}