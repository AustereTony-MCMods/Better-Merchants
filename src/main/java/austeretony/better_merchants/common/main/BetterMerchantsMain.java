package austeretony.better_merchants.common.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.client.command.CommandBetterMerchantsClient;
import austeretony.better_merchants.client.event.MerchantsEventsClient;
import austeretony.better_merchants.client.gui.CurrencyHelper;
import austeretony.better_merchants.client.gui.settings.GUISettings;
import austeretony.better_merchants.common.CommonReference;
import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.capability.IMerchant;
import austeretony.better_merchants.common.capability.Merchant;
import austeretony.better_merchants.common.capability.MerchantStorage;
import austeretony.better_merchants.common.command.CommandBetterMerchantsServer;
import austeretony.better_merchants.common.event.MerchantsEventsServer;
import austeretony.better_merchants.common.network.Network;
import austeretony.better_merchants.common.network.client.CPCommand;
import austeretony.better_merchants.common.network.client.CPOpenMerchantMenu;
import austeretony.better_merchants.common.network.client.CPSyncEntityEntry;
import austeretony.better_merchants.common.network.client.CPSyncMainData;
import austeretony.better_merchants.common.network.client.CPSyncMerchantProfile;
import austeretony.better_merchants.common.network.client.CPSyncMerchantProfiles;
import austeretony.better_merchants.common.network.client.CPSyncValidProfileIds;
import austeretony.better_merchants.common.network.client.CPUpdateMerchantMenu;
import austeretony.better_merchants.common.network.client.CPVerifyEntityId;
import austeretony.better_merchants.common.network.server.SPCreateMerchant;
import austeretony.better_merchants.common.network.server.SPCreateProfile;
import austeretony.better_merchants.common.network.server.SPMerchantOperation;
import austeretony.better_merchants.common.network.server.SPOpenMerchantMenu;
import austeretony.better_merchants.common.network.server.SPRemoveProfile;
import austeretony.better_merchants.common.network.server.SPRequestEntityEntrySync;
import austeretony.better_merchants.common.network.server.SPRequestMerchantProfileSync;
import austeretony.better_merchants.common.network.server.SPSendAbsentProfileIds;
import austeretony.better_merchants.common.network.server.SPSendMerchantProfile;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
        modid = BetterMerchantsMain.MODID, 
        name = BetterMerchantsMain.NAME, 
        version = BetterMerchantsMain.VERSION,
        dependencies = "required-after:grandeconomy@[1.2.0,);",
        certificateFingerprint = "@FINGERPRINT@",
        updateJSON = BetterMerchantsMain.VERSIONS_FORGE_URL)
public class BetterMerchantsMain {

    public static final String 
    MODID = "better_merchants", 
    NAME = "Better Merchants", 
    VERSION = "1.0.0", 
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Better-Merchants/info/mod_versions_forge.json";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    private static Network network;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        this.initNetwork();
        MerchantsManagerServer.create();
        MerchantsManagerServer.instance().loadProfiles();
        CommonReference.registerEvent(new MerchantsEventsServer());  
        CommonReference.registerEvent(new SoundEffects());  
        CommonReference.registerEvent(new CurrencyHandler());  
        CapabilityManager.INSTANCE.register(IMerchant.class, new MerchantStorage(), Merchant::new);
        if (event.getSide() == Side.CLIENT) {
            GUISettings.create();
            MerchantsManagerClient.create();
            CommonReference.registerEvent(new MerchantsEventsClient());  
            ClientReference.registerCommand(new CommandBetterMerchantsClient());
        }
    }

    @EventHandler
    public void init(FMLPostInitializationEvent event) {
        if (event.getSide() == Side.CLIENT)
            CurrencyHelper.create();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        String 
        worldName = event.getServer().getFolderName(),
        worldFolder = event.getServer().isSinglePlayer() ? CommonReference.getGameFolder() + "/saves/" + worldName : CommonReference.getGameFolder() + "/" + worldName;
        LOGGER.info("Initializing world: {}.", worldName);
        MerchantsManagerServer.instance().load(worldFolder);
        CommonReference.registerCommand(event, new CommandBetterMerchantsServer());
    }

    private void initNetwork() {
        network = Network.createNetworkHandler(MODID);

        network.registerPacket(CPCommand.class);
        network.registerPacket(CPSyncMainData.class);
        network.registerPacket(CPSyncValidProfileIds.class);
        network.registerPacket(CPSyncMerchantProfiles.class);
        network.registerPacket(CPUpdateMerchantMenu.class);
        network.registerPacket(CPVerifyEntityId.class);
        network.registerPacket(CPSyncEntityEntry.class);
        network.registerPacket(CPSyncMerchantProfile.class);
        network.registerPacket(CPOpenMerchantMenu.class);

        network.registerPacket(SPSendAbsentProfileIds.class);
        network.registerPacket(SPCreateProfile.class);
        network.registerPacket(SPRemoveProfile.class);
        network.registerPacket(SPSendMerchantProfile.class);
        network.registerPacket(SPMerchantOperation.class);
        network.registerPacket(SPCreateMerchant.class);
        network.registerPacket(SPOpenMerchantMenu.class);
        network.registerPacket(SPRequestEntityEntrySync.class);
        network.registerPacket(SPRequestMerchantProfileSync.class);
    }

    public static Network network() {
        return network;
    }
}