package austeretony.better_merchants.common.network;

import java.io.IOException;

import com.google.common.collect.HashBiMap;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class Network {

    public final String channelName;

    public final FMLEventChannel channel;

    private final HashBiMap<Integer, Class<? extends ProxyPacket>> packets = HashBiMap.<Integer, Class<? extends ProxyPacket>>create();

    private int id;

    private Network(String channelName) {
        this.channelName = channelName;
        this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName);
        this.channel.register(this);
    }

    public static Network createNetworkHandler(String channelName) {
        return new Network(channelName);
    }

    public void registerPacket(Class<? extends ProxyPacket> packet) {
        this.packets.put(this.id++, packet);
    }

    public void registerPacketCheckExisted(Class<? extends ProxyPacket> packet) {
        if (!this.packets.containsValue(packet))
            this.packets.put(this.id++, packet);
    }

    @SubscribeEvent
    public void onClientPacketRecieve(FMLNetworkEvent.ClientCustomPacketEvent event) throws IOException {
        process(event);
    }

    @SubscribeEvent
    public void onServerPacketRecieve(FMLNetworkEvent.ServerCustomPacketEvent event) throws IOException {
        process(event);
    }

    private FMLProxyPacket pack(ProxyPacket packet, INetHandler netHandler) {
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeByte(this.packets.inverse().get(packet.getClass()));
        packet.write(packetBuffer, netHandler);
        return new FMLProxyPacket(packetBuffer, this.channelName);
    }

    private void process(FMLNetworkEvent.CustomPacketEvent event) throws IOException {
        FMLProxyPacket packet = event.getPacket();
        if (this.channelName.equals(packet.channel())) {
            if (packet.payload().readableBytes() != 0) {
                PacketBuffer buffer = new PacketBuffer(packet.payload());
                ProxyPacket.create(this.packets, buffer.readByte()).read(buffer, event.getHandler());
            }
        }
    }

    public void sendToServer(ProxyPacket packet) {
        this.channel.sendToServer(this.pack(packet, null));
    }

    public void sendTo(ProxyPacket packet, EntityPlayerMP player) {
        this.channel.sendTo(this.pack(packet, player.connection.netManager.getNetHandler()), player);
    }

    public void sendToAll(ProxyPacket packet) {
        this.channel.sendToAll(this.pack(packet, null));
    }

    public void sendToAllAround(ProxyPacket packet, TargetPoint point) {
        this.channel.sendToAllAround(this.pack(packet, null), point);
    }

    public void sendToAllTracking(ProxyPacket packet, Entity entity) {
        this.channel.sendToAllTracking(this.pack(packet, null), entity);
    }

    public void sendToAllTracking(ProxyPacket packet, TargetPoint point) {
        this.channel.sendToAllTracking(this.pack(packet, null), point);
    }
}
