package austeretony.better_merchants.common.network.client;

import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPCommand extends ProxyPacket {

    private int ordinal;

    public CPCommand() {}

    public CPCommand(EnumCommand command) {
        this.ordinal = command.ordinal();
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.ordinal);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        switch (EnumCommand.values()[buffer.readByte()]) {
        case OPEN_MANAGEMENT_MENU:
            MerchantsManagerClient.instance().openManagementMenu();
            break;
        }
    }

    public enum EnumCommand {

        OPEN_MANAGEMENT_MENU
    }
}