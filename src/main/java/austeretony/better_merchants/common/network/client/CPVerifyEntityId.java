package austeretony.better_merchants.common.network.client;

import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPVerifyEntityId extends ProxyPacket {

    private long entryId, profileId;

    public CPVerifyEntityId() {}

    public CPVerifyEntityId(long entityId, long profileId) {
        this.entryId = entityId;
        this.profileId = profileId;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeLong(this.entryId);
        buffer.writeLong(this.profileId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerClient.instance().getEntitiesManager().checkMerchantDataExist(buffer.readLong(), buffer.readLong());
    }
}