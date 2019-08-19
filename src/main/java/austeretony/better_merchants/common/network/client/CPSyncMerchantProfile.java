package austeretony.better_merchants.common.network.client;

import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.common.MerchantsLoader;
import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.main.MerchantProfile;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncMerchantProfile extends ProxyPacket {

    private long profileId;

    public CPSyncMerchantProfile() {}

    public CPSyncMerchantProfile(long profileId) {
        this.profileId = profileId;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerServer.instance().getEntitiesManager().getProfile(this.profileId).write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerClient.instance().getEntitiesManager().cacheProfile(MerchantProfile.read(buffer));
        if (MerchantsManagerClient.instance().getWorldId() != 0L)
            MerchantsLoader.savePersistentData(MerchantsManagerClient.instance().getEntitiesManager());
    }
}