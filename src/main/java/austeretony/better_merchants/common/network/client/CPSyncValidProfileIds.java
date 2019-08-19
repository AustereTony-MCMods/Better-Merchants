package austeretony.better_merchants.common.network.client;

import java.util.Set;

import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.client.gui.management.ManagementMenuGUIScreen;
import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.main.MerchantProfile;
import austeretony.better_merchants.common.network.ProxyPacket;
import austeretony.better_merchants.common.network.server.SPSendAbsentProfileIds;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncValidProfileIds extends ProxyPacket {

    public CPSyncValidProfileIds() {}

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        Set<Long> ids = MerchantsManagerServer.instance().getMerchantProfilesManager().getProfilesIds();
        buffer.writeShort(ids.size());
        for (long id : ids)
            buffer.writeLong(id);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        long[] syncedIds = new long[buffer.readShort()];
        int 
        i = 0, 
        j = 0;
        for (; i < syncedIds.length; i++)
            syncedIds[i] = buffer.readLong();
        long[] needSync = new long[syncedIds.length];
        Set<Long> clientIds = MerchantsManagerClient.instance().getMerchantProfilesManager().getProfilesIds();
        MerchantProfile[] validEntries = new MerchantProfile[syncedIds.length];
        i = 0;
        for (long entryId : syncedIds)
            if (!clientIds.contains(entryId))
                needSync[i++] = entryId;    
            else
                validEntries[j++] = MerchantsManagerClient.instance().getMerchantProfilesManager().getProfile(entryId);
        MerchantsManagerClient.instance().getMerchantProfilesManager().reset();
        for (MerchantProfile validEntry : validEntries) {
            if (validEntry == null) break;
            MerchantsManagerClient.instance().getMerchantProfilesManager().addProfile(validEntry);
        }
        if (i == 0)
            ManagementMenuGUIScreen.dataReceived = true;
        BetterMerchantsMain.network().sendToServer(new SPSendAbsentProfileIds(needSync, i));
    }
}