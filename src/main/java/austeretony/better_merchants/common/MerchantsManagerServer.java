package austeretony.better_merchants.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.main.CurrencyHandler;
import austeretony.better_merchants.common.main.OperationsProcessor;
import austeretony.better_merchants.common.main.OperationsProcessor.EnumOperation;
import austeretony.better_merchants.common.network.client.CPSyncMainData;
import net.minecraft.entity.player.EntityPlayerMP;

public final class MerchantsManagerServer {

    private static MerchantsManagerServer instance;

    private long worldId;

    private String configFolder, worldFolder;

    private MerchantProfilesManagerServer profilesManager;

    private EntitiesManagerServer entitiesManager;

    private final Map<UUID, OperationsProcessor> containers = new HashMap<UUID, OperationsProcessor>();

    private MerchantsManagerServer() {
        this.profilesManager = new MerchantProfilesManagerServer();
        this.entitiesManager = new EntitiesManagerServer();
    }

    public static void create() {
        if (instance == null)
            instance = new MerchantsManagerServer();
    }

    public static MerchantsManagerServer instance() {
        return instance;
    }

    public MerchantProfilesManagerServer getMerchantProfilesManager() {
        return this.profilesManager;
    }

    public EntitiesManagerServer getEntitiesManager() {
        return this.entitiesManager;
    }

    public void loadProfiles() {
        this.configFolder = CommonReference.getGameFolder() + "/config/better merchants/server/";
        MerchantsLoader.loadPersistentData(this.profilesManager);
        this.profilesManager.cacheProfiles();
    }

    public void load(String worldFolder) {
        this.createWorldId(worldFolder);
        this.entitiesManager.reset();
    }

    private void createWorldId(String worldFolder) {
        String 
        worldIdFilePathStr = worldFolder + "/better merchants/worldid.txt",
        worldIdStr;
        Path worldIdPath = Paths.get(worldIdFilePathStr);
        if (Files.exists(worldIdPath)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(worldIdFilePathStr))) {  
                worldIdStr = bufferedReader.readLine();
                this.worldId = Long.parseLong(worldIdStr);
                BetterMerchantsMain.LOGGER.info("Loaded world id: {}.", worldIdStr);
            } catch (IOException exception) {
                BetterMerchantsMain.LOGGER.error("World id loading failed.");
                exception.printStackTrace();
            }           
        } else {
            this.worldId = Long.parseLong(new SimpleDateFormat("yyMMddHHmmss").format(new Date()));
            worldIdStr = String.valueOf(this.worldId);
            BetterMerchantsMain.LOGGER.info("Created world id: {}.", worldIdStr);
            try {               
                Files.createDirectories(worldIdPath.getParent());             
                try (PrintStream printStream = new PrintStream(new File(worldIdFilePathStr))) {
                    printStream.println(worldIdStr);
                } 
            } catch (IOException exception) {      
                BetterMerchantsMain.LOGGER.error("World id saving failed.");
                exception.printStackTrace();
            }
        }
        this.worldFolder = worldFolder + "/better merchants/server/";
    }

    public void runMerchantOperations() {
        for (OperationsProcessor container : this.containers.values())
            container.process();
    }

    public void onPlayerLoggedIn(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        BetterMerchantsMain.network().sendTo(new CPSyncMainData(this.worldId), playerMP); 
        this.containers.put(playerUUID, new OperationsProcessor(playerUUID));
        CurrencyHandler.updateBalance(playerMP);     
    }

    public void onPlayerLoggedOut(EntityPlayerMP playerMP) {
        this.containers.remove(CommonReference.getPersistentUUID(playerMP));
    }

    public void performOperation(EntityPlayerMP playerMP, EnumOperation operation, long profileId, long offerId) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        if (this.containers.containsKey(playerUUID))
            this.containers.get(playerUUID).addOperation(operation, profileId, offerId);
    }

    public long getWorldId() {
        return this.worldId;
    }

    public String getConfigFolder() {
        return this.configFolder;
    }

    public String getWorldFolder() {
        return this.worldFolder;
    }
}