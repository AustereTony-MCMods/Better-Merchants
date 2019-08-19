package austeretony.better_merchants.common.network.server;

import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.network.ProxyPacket;
import austeretony.better_merchants.common.util.PacketBufferUtils;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPCreateMerchant extends ProxyPacket {

    private long entryId;

    private int entityId;

    private String name, profession;

    private long profileId;

    public SPCreateMerchant() {}

    public SPCreateMerchant(long entryId, int entityId, String name, String profession, long profileId) {
        this.entryId = entryId;
        this.entityId = entityId;
        this.name = name;
        this.profession = profession;
        this.profileId = profileId;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeLong(this.entryId);
        buffer.writeInt(this.entityId);
        PacketBufferUtils.writeString(this.name, buffer);
        PacketBufferUtils.writeString(this.profession, buffer);
        buffer.writeLong(this.profileId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerServer.instance().getEntitiesManager().createMerchant(getEntityPlayerMP(netHandler), 
                buffer.readLong(), buffer.readInt(), PacketBufferUtils.readString(buffer), PacketBufferUtils.readString(buffer), buffer.readLong());
    }
}
