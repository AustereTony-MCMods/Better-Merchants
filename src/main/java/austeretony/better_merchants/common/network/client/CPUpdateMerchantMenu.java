package austeretony.better_merchants.common.network.client;

import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.common.main.OperationsProcessor;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPUpdateMerchantMenu extends ProxyPacket {

    private OperationsProcessor.EnumOperation operation;

    public CPUpdateMerchantMenu() {}

    public CPUpdateMerchantMenu(OperationsProcessor.EnumOperation operation) {
        this.operation = operation;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.operation.ordinal());
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        MerchantsManagerClient.instance().updateMerchantMenu(OperationsProcessor.EnumOperation.values()[buffer.readByte()]);
    }
}
