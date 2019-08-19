package austeretony.better_merchants.common.network.client;

import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.client.gui.management.ManagementMenuGUIScreen;
import austeretony.better_merchants.common.MerchantsLoader;
import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.main.MerchantProfile;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncMerchantProfiles extends ProxyPacket {

    private long[] ids;

    public CPSyncMerchantProfiles() {}

    public CPSyncMerchantProfiles(long[] ids) {
        this.ids = ids;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeShort(this.ids.length);
        for (long id : this.ids)
            MerchantsManagerServer.instance().getMerchantProfilesManager().getProfile(id).write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int amount = buffer.readShort();        
        for (int i = 0; i < amount; i++)
            MerchantsManagerClient.instance().getMerchantProfilesManager().addProfile(MerchantProfile.read(buffer));
        MerchantsLoader.savePersistentData(MerchantsManagerClient.instance().getMerchantProfilesManager());
        ManagementMenuGUIScreen.dataReceived = true;
    }
}