package austeretony.better_merchants.common.network.client;

import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.common.MerchantsLoader;
import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.main.EntityEntry;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncEntityEntry extends ProxyPacket {

    private long entryId;

    public CPSyncEntityEntry() {}

    public CPSyncEntityEntry(long entryId) {
        this.entryId = entryId;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerServer.instance().getEntitiesManager().getEntityEntry(this.entryId).write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerClient.instance().getEntitiesManager().addEntityEntry(EntityEntry.read(buffer));
        if (MerchantsManagerClient.instance().getWorldId() != 0L)
            MerchantsLoader.savePersistentData(MerchantsManagerClient.instance().getEntitiesManager());
    }
}