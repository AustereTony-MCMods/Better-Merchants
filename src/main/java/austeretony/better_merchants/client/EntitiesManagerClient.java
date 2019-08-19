package austeretony.better_merchants.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import austeretony.better_merchants.common.IPersistentData;
import austeretony.better_merchants.common.MerchantsLoader;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.main.EntityEntry;
import austeretony.better_merchants.common.main.MerchantProfile;
import austeretony.better_merchants.common.network.server.SPCreateMerchant;
import austeretony.better_merchants.common.network.server.SPRequestEntityEntrySync;
import austeretony.better_merchants.common.network.server.SPRequestMerchantProfileSync;
import austeretony.better_merchants.common.util.StreamUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntitiesManagerClient implements IPersistentData {

    private final Map<Long, EntityEntry> entries = new HashMap<Long, EntityEntry>();

    private final Map<UUID, Long> access = new HashMap<UUID, Long>();

    private final Map<Long, MerchantProfile> cachedProfiles = new HashMap<Long, MerchantProfile>();

    public boolean entryExist(long entryId) {
        return this.entries.containsKey(entryId);
    }

    public boolean entryExist(UUID entityUUID) {
        return this.access.containsKey(entityUUID);
    }

    public EntityEntry getEntityEntry(long entryId) {
        return this.entries.get(entryId);
    }

    public EntityEntry getEntityEntry(UUID entityUUID) {
        return this.entries.get(this.access.get(entityUUID));
    }

    public UUID getEntityUUIDById(long entryId) {
        return this.entries.get(entryId).entityUUID;
    }

    public void addEntityEntry(EntityEntry entry) {
        this.entries.put(entry.entryId, entry);
        this.access.put(entry.entityUUID, entry.entryId);
    }

    public void cacheProfile(MerchantProfile profile) {
        this.cachedProfiles.put(profile.getId(), profile);
    }

    public MerchantProfile getProfile(long profileId) {
        return this.cachedProfiles.get(profileId);
    }

    public void createMerchantSynced(Entity entity, String name, String profession, long profileId) {
        ((EntityLiving) entity).enablePersistence();
        EntityEntry entry = new EntityEntry(ClientReference.getPersistentUUID(entity), System.currentTimeMillis(), profileId, name, profession);
        this.addEntityEntry(entry);
        this.cacheProfile(MerchantsManagerClient.instance().getMerchantProfilesManager().getProfile(profileId).copy());
        BetterMerchantsMain.network().sendToServer(new SPCreateMerchant(entry.entryId, entity.getEntityId(), name, profession, profileId));
        MerchantsLoader.savePersistentData(this);
    }

    public void checkMerchantDataExist(long entryId, long profileId) {
        if (!this.entries.containsKey(entryId))
            BetterMerchantsMain.network().sendToServer(new SPRequestEntityEntrySync(entryId));
        if (!this.cachedProfiles.containsKey(profileId))
            BetterMerchantsMain.network().sendToServer(new SPRequestMerchantProfileSync(profileId));
    }

    @Override
    public String getPath() {
        return MerchantsManagerClient.instance().getWorldFolder() + "merchants.dat";
    }

    @Override
    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write((short) this.entries.size(), bos);
        for (EntityEntry entry : this.entries.values())
            entry.write(bos);

        StreamUtils.write((short) this.cachedProfiles.size(), bos);
        for (MerchantProfile profile : this.cachedProfiles.values())
            profile.write(bos);
    }

    @Override
    public void read(BufferedInputStream bis) throws IOException {
        int 
        amount = StreamUtils.readShort(bis),
        i = 0;
        EntityEntry entityEntry;
        for (; i < amount; i++) {
            entityEntry = EntityEntry.read(bis);
            this.entries.put(entityEntry.entryId, entityEntry);
            this.access.put(entityEntry.entityUUID, entityEntry.entryId);
        }

        amount = StreamUtils.readShort(bis);
        MerchantProfile profile;
        for (i = 0; i < amount; i++) {
            profile = MerchantProfile.read(bis);
            this.cachedProfiles.put(profile.getId(), profile);
        }
    }

    public void reset() {
        this.entries.clear();
        this.access.clear();
        this.cachedProfiles.clear();
    }
}