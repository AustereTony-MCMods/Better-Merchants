package austeretony.better_merchants.common.event;

import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.capability.MerchantProvider;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class MerchantsEventsServer {

    public static final ResourceLocation MERCHANT = new ResourceLocation(BetterMerchantsMain.MODID, "Merchant");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent event) {
        if (event.getObject() instanceof EntityLiving)          
            event.addCapability(MERCHANT, new MerchantProvider());                        
    }

    @SubscribeEvent
    public void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof EntityLiving)
            MerchantsManagerServer.instance().getEntitiesManager().playerStartTrackingEntity(event.getTarget(), (EntityPlayerMP) event.getEntityPlayer());
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            MerchantsManagerServer.instance().runMerchantOperations();
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {        
        MerchantsManagerServer.instance().onPlayerLoggedIn((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {        
        MerchantsManagerServer.instance().onPlayerLoggedOut((EntityPlayerMP) event.player);
    }
}