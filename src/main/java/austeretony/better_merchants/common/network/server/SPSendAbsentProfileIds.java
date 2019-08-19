package austeretony.better_merchants.common.network.server;

import austeretony.better_merchants.common.CommonReference;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.network.ProxyPacket;
import austeretony.better_merchants.common.network.client.CPCommand;
import austeretony.better_merchants.common.network.client.CPSyncMerchantProfiles;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPSendAbsentProfileIds extends ProxyPacket {

    private long[] ids;

    private int size;

    public SPSendAbsentProfileIds() {}

    public SPSendAbsentProfileIds(long[] ids, int size) {
        this.ids = ids;
        this.size = size;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeShort(this.size);
        for (long entryId : this.ids) {
            if (entryId == 0L) break;
            buffer.writeLong(entryId);
        }
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (CommonReference.isOpped(playerMP)) {
            int amount = buffer.readShort();
            if (amount > 0) {
                long[] needSync = new long[amount];
                int index = 0;
                for (int i = 0; i < amount; i++)
                    needSync[index++] = buffer.readLong();
                BetterMerchantsMain.network().sendTo(new CPSyncMerchantProfiles(needSync), playerMP);
            }
            BetterMerchantsMain.network().sendTo(new CPCommand(CPCommand.EnumCommand.OPEN_MANAGEMENT_MENU), playerMP);
        }
    }
}