package austeretony.better_merchants.common.capability;

import austeretony.better_merchants.common.main.EntityEntry;
import austeretony.better_merchants.common.main.MerchantProfile;

public interface IMerchant {

    boolean available();
    
    void setAvailable();

    EntityEntry getEntityEntry();

    void setEntityEntry(EntityEntry entry);

    MerchantProfile getMerchantProfile();

    void setMerchantProfile(MerchantProfile profile);
}