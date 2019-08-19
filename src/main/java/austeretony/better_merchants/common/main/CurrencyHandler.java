package austeretony.better_merchants.common.main;

import austeretony.better_merchants.common.CommonReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import the_fireplace.grandeconomy.api.GrandEconomyApi;

public class CurrencyHandler {

    public static final DataParameter<Integer> CURRENCY = EntityDataManager.<Integer>createKey(EntityPlayer.class, DataSerializers.VARINT);

    @SubscribeEvent
    public void onPlayerConstructing(EntityConstructing event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity(); 
            player.getDataManager().register(CURRENCY, 0);                          
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        updateBalance(event.getEntityPlayer());
    }

    public static int getCurrency(EntityPlayer player) {
        return player.getDataManager().get(CURRENCY);
    }

    public static void updateBalance(EntityPlayer player) {        
        player.getDataManager().set(CURRENCY, (int) GrandEconomyApi.getBalance(CommonReference.getPersistentUUID(player)));
    }
}