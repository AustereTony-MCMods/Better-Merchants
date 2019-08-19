package austeretony.better_merchants.common.main;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoundEffects {

    public static final SoundEventContainer 
    INVENTORY = new SoundEventContainer(BetterMerchantsMain.MODID, "inventory"),
    SELL = new SoundEventContainer(BetterMerchantsMain.MODID, "sell"),
    BUTTON_CLICK = new SoundEventContainer(BetterMerchantsMain.MODID, "button_click"),
    CONTEXT_OPEN = new SoundEventContainer(BetterMerchantsMain.MODID, "context_open"),
    CONTEXT_CLOSE = new SoundEventContainer(BetterMerchantsMain.MODID, "context_close");

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                INVENTORY.soundEvent,
                SELL.soundEvent,
                BUTTON_CLICK.soundEvent,
                CONTEXT_OPEN.soundEvent,
                CONTEXT_CLOSE.soundEvent
                );
    }

    public static class SoundEventContainer {

        public final SoundEvent soundEvent;

        public SoundEventContainer(String modId, String name) {
            ResourceLocation location = new ResourceLocation(modId, name);
            this.soundEvent = new SoundEvent(location).setRegistryName(location);
        }
    }
}
