package austeretony.better_merchants.common.network.client;

import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPOpenMerchantMenu extends ProxyPacket {

    private long profileId;

    public CPOpenMerchantMenu() {}

    public CPOpenMerchantMenu(long profileId) {
        this.profileId = profileId;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeLong(this.profileId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerClient.instance().openMerchantMenu(buffer.readLong());
    }
}