package austeretony.better_merchants.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import austeretony.better_merchants.common.capability.IMerchant;
import austeretony.better_merchants.common.capability.MerchantProvider;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.main.CurrencyHandler;
import austeretony.better_merchants.common.main.EntityEntry;
import austeretony.better_merchants.common.main.MerchantProfile;
import austeretony.better_merchants.common.network.client.CPOpenMerchantMenu;
import austeretony.better_merchants.common.network.client.CPSyncEntityEntry;
import austeretony.better_merchants.common.network.client.CPSyncMerchantProfile;
import austeretony.better_merchants.common.network.client.CPVerifyEntityId;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;

public final class EntitiesManagerServer {

    private final Map<Long, EntityEntry> entries = new HashMap<Long, EntityEntry>();

    private final Map<UUID, Long> access = new HashMap<UUID, Long>();

    private final Map<Long, MerchantProfile> cachedProfiles = new HashMap<Long, MerchantProfile>();

    public void createMerchant(EntityPlayerMP playerMP, long entryId, int entityId, String name, String profession, long profileId) {
        if (CommonReference.isOpped(playerMP)) {
            Entity pointed = playerMP.world.getEntityByID(entityId);
            if (pointed != null 
                    && pointed instanceof EntityLiving
                    && MerchantsManagerServer.instance().getMerchantProfilesManager().profileExist(profileId)) {
                ((EntityLiving) pointed).enablePersistence();
                IMerchant merchant = pointed.getCapability(MerchantProvider.MERCHANT, null);
                merchant.setAvailable();
                merchant.setEntityEntry(new EntityEntry(CommonReference.getPersistentUUID(pointed), entryId, profileId, name, profession));
                merchant.setMerchantProfile(MerchantsManagerServer.instance().getMerchantProfilesManager().getProfile(profileId).copy());
                this.addEntityEntry(merchant.getEntityEntry());
                this.cacheProfile(merchant.getMerchantProfile().copy());
            }
        }
    }

    public void playerStartTrackingEntity(Entity entity, EntityPlayerMP playerMP) {
        IMerchant merchant = entity.getCapability(MerchantProvider.MERCHANT, null);
        if (merchant.available()) {
            this.addEntityEntry(merchant.getEntityEntry());
            this.cacheProfile(merchant.getMerchantProfile().copy());
            BetterMerchantsMain.network().sendTo(new CPVerifyEntityId(merchant.getEntityEntry().entryId, merchant.getMerchantProfile().getId()), playerMP);
        }
    }

    public void syncEntityEntry(EntityPlayerMP playerMP, long entryId) {
        BetterMerchantsMain.network().sendTo(new CPSyncEntityEntry(entryId), playerMP);
    }

    public void syncMerchantProfile(EntityPlayerMP playerMP, long profileId) {
        BetterMerchantsMain.network().sendTo(new CPSyncMerchantProfile(profileId), playerMP);
    }

    public void addEntityEntry(EntityEntry entry) {
        if (!this.entries.containsKey(entry.entryId)) {
            this.entries.put(entry.entryId, entry);
            this.access.put(entry.entityUUID, entry.entryId);
        }
    }

    public EntityEntry getEntityEntry(long entryId) {
        return this.entries.get(entryId);
    }

    public boolean profileExist(long profileId) {
        return this.cachedProfiles.containsKey(profileId);
    }

    public void cacheProfile(MerchantProfile profile) {
        if (!this.cachedProfiles.containsKey(profile.getId()))
            this.cachedProfiles.put(profile.getId(), profile);
    }

    public void removeProfile(long profileId) {
        this.cachedProfiles.remove(profileId);
    }

    public MerchantProfile getProfile(long profileId) {
        return this.cachedProfiles.get(profileId);
    }

    public void openMerchantMenu(EntityPlayerMP playerMP, int entityId) {
        Entity entity = CommonReference.getEntityById(playerMP, entityId);
        if (entity != null 
                && entity instanceof EntityLiving
                && CommonReference.isEntitiesNear(playerMP, entity, 5.0D)) {
            CurrencyHandler.updateBalance(playerMP);
            UUID entityUUID = CommonReference.getPersistentUUID(entity);
            if (this.access.containsKey(entityUUID))
                BetterMerchantsMain.network().sendTo(new CPOpenMerchantMenu(this.entries.get(this.access.get(entityUUID)).profileId), playerMP); 
        }
    }

    public void reset() {
        this.entries.clear();
        this.access.clear();
    }
}