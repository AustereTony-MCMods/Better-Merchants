package austeretony.better_merchants.common.network.server;

import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPOpenMerchantMenu extends ProxyPacket {

    private int entityId;

    public SPOpenMerchantMenu() {}

    public SPOpenMerchantMenu(int entityId) {
        this.entityId = entityId;
    }

    @Override 
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeInt(this.entityId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerServer.instance().getEntitiesManager().openMerchantMenu(getEntityPlayerMP(netHandler), buffer.readInt());
    }
}
