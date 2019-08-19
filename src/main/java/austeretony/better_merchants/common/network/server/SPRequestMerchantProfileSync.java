package austeretony.better_merchants.common.network.server;

import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPRequestMerchantProfileSync extends ProxyPacket {

    private long profileId;

    public SPRequestMerchantProfileSync() {}

    public SPRequestMerchantProfileSync(long profileId) {
        this.profileId = profileId;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeLong(this.profileId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerServer.instance().getEntitiesManager().syncMerchantProfile(getEntityPlayerMP(netHandler), buffer.readLong());
    }
}