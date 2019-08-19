package austeretony.better_merchants.common.network.client;

import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncMainData extends ProxyPacket {

    private long worldId;

    public CPSyncMainData() {}

    public CPSyncMainData(long worldId) {
        this.worldId = worldId;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeLong(this.worldId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerClient.instance().setWorldId(buffer.readLong());
    }
}