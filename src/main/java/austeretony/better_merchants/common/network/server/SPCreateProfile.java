package austeretony.better_merchants.common.network.server;

import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.network.ProxyPacket;
import austeretony.better_merchants.common.util.PacketBufferUtils;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPCreateProfile extends ProxyPacket {

    private long id;

    private String name;

    public SPCreateProfile() {}

    public SPCreateProfile(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeLong(this.id);
        PacketBufferUtils.writeString(this.name, buffer);
    }

    @Override   
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerServer.instance().getMerchantProfilesManager().createProfile(getEntityPlayerMP(netHandler), buffer.readLong(), PacketBufferUtils.readString(buffer));
    }
}
