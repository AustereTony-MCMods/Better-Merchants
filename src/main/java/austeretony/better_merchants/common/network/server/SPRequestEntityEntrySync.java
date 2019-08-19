package austeretony.better_merchants.common.network.server;

import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPRequestEntityEntrySync extends ProxyPacket {

    private long entryId;

    public SPRequestEntityEntrySync() {}

    public SPRequestEntityEntrySync(long entryId) {
        this.entryId = entryId;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeLong(this.entryId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerServer.instance().getEntitiesManager().syncEntityEntry(getEntityPlayerMP(netHandler), buffer.readLong());
    }
}