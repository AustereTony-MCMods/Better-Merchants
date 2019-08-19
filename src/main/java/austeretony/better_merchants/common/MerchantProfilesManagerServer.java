package austeretony.better_merchants.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import austeretony.better_merchants.common.main.MerchantProfile;
import austeretony.better_merchants.common.util.StreamUtils;
import net.minecraft.entity.player.EntityPlayerMP;

public final class MerchantProfilesManagerServer implements IPersistentData {

    private final Map<Long, MerchantProfile> merchantProfiles = new HashMap<Long, MerchantProfile>();

    public int getProfilesAmount() {
        return this.merchantProfiles.size();
    }

    public Set<Long> getProfilesIds() {
        return this.merchantProfiles.keySet();
    }

    public Collection<MerchantProfile> getProfiles() {
        return this.merchantProfiles.values();
    }

    public boolean profileExist(long profileId) {
        return this.merchantProfiles.containsKey(profileId);
    }

    public MerchantProfile getProfile(long profileId) {
        return this.merchantProfiles.get(profileId);
    }   

    public void createProfile(EntityPlayerMP playerMP, long profileId, String name) {
        if (CommonReference.isOpped(playerMP)) {
            MerchantProfile profile = new MerchantProfile();
            profile.setId(profileId);
            profile.setName(name);
            profile.setUseCurrency(true);
            this.merchantProfiles.put(profileId, profile);
            MerchantsManagerServer.instance().getEntitiesManager().cacheProfile(profile.copy());
            MerchantsLoader.savePersistentData(this);
        }
    }

    public void editProfile(EntityPlayerMP playerMP, long oldProfileId, MerchantProfile profile) {
        if (CommonReference.isOpped(playerMP)) {
            this.merchantProfiles.remove(oldProfileId);
            this.merchantProfiles.put(profile.getId(), profile);
            MerchantsManagerServer.instance().getEntitiesManager().removeProfile(oldProfileId);
            MerchantsManagerServer.instance().getEntitiesManager().cacheProfile(profile);
            MerchantsLoader.savePersistentData(this);
        }
    }   

    public void removeProfile(EntityPlayerMP playerMP, long profileId) {
        if (CommonReference.isOpped(playerMP)) {
            if (this.profileExist(profileId)) {
                this.merchantProfiles.remove(profileId);
                MerchantsManagerServer.instance().getEntitiesManager().removeProfile(profileId);
                MerchantsLoader.savePersistentData(this);
            }
        }
    }

    public void cacheProfiles() {
        for (MerchantProfile profile : this.merchantProfiles.values())
            MerchantsManagerServer.instance().getEntitiesManager().cacheProfile(profile.copy());
    }

    @Override
    public String getPath() {
        return MerchantsManagerServer.instance().getConfigFolder() + "profiles.dat";
    }

    @Override
    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write((short) this.merchantProfiles.size(), bos);
        for (MerchantProfile profile : this.merchantProfiles.values())
            profile.write(bos);
    }

    @Override
    public void read(BufferedInputStream bis) throws IOException { 
        int amount = StreamUtils.readShort(bis);
        MerchantProfile profile;
        for (int i = 0; i < amount; i++) {
            profile = MerchantProfile.read(bis);
            this.merchantProfiles.put(profile.getId(), profile);
        }
    }

    public void reset() {
        this.merchantProfiles.clear();
    }
}