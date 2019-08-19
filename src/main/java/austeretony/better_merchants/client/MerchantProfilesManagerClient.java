package austeretony.better_merchants.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import austeretony.better_merchants.common.IPersistentData;
import austeretony.better_merchants.common.MerchantsLoader;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.main.MerchantProfile;
import austeretony.better_merchants.common.network.server.SPCreateProfile;
import austeretony.better_merchants.common.network.server.SPRemoveProfile;
import austeretony.better_merchants.common.network.server.SPSendMerchantProfile;
import austeretony.better_merchants.common.util.StreamUtils;

public class MerchantProfilesManagerClient implements IPersistentData {

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

    public void addProfile(MerchantProfile profile) {
        this.merchantProfiles.put(profile.getId(), profile);
    }

    public void removeProfile(long profileId) {
        this.merchantProfiles.remove(profileId);
    }

    public void createProfileSynced(String name) {
        MerchantProfile profile = new MerchantProfile();
        profile.createId();
        profile.setName(name);
        profile.setUseCurrency(true);
        this.merchantProfiles.put(profile.getId(), profile);
        BetterMerchantsMain.network().sendToServer(new SPCreateProfile(profile.getId(), name));
        MerchantsLoader.savePersistentData(this);
    }

    public void saveProfileChangesSynced(MerchantProfile changesBuffer) {
        long oldProfileId = changesBuffer.getId();
        this.removeProfile(oldProfileId);
        changesBuffer.setId(oldProfileId + 1L);
        this.addProfile(changesBuffer);
        BetterMerchantsMain.network().sendToServer(new SPSendMerchantProfile(oldProfileId));
        MerchantsLoader.savePersistentData(this);        
    }

    public void removeProfileSynced(long profileId) {
        this.removeProfile(profileId);
        BetterMerchantsMain.network().sendToServer(new SPRemoveProfile(profileId));
        MerchantsLoader.savePersistentData(this);       
    }


    @Override
    public String getPath() {
        return MerchantsManagerClient.instance().getWorldFolder() + "profiles.dat";
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