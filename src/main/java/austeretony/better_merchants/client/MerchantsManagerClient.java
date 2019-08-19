package austeretony.better_merchants.client;

import austeretony.better_merchants.client.gui.management.ManagementMenuGUIScreen;
import austeretony.better_merchants.client.gui.merchant.MerchantMenuGUIScreen;
import austeretony.better_merchants.common.CommonReference;
import austeretony.better_merchants.common.MerchantsLoader;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.main.OperationsProcessor;
import austeretony.better_merchants.common.network.server.SPMerchantOperation;
import austeretony.better_merchants.common.network.server.SPOpenMerchantMenu;

public class MerchantsManagerClient {

    private static MerchantsManagerClient instance;

    private long worldId;

    private String worldFolder;

    private MerchantProfilesManagerClient profilesManager;

    private EntitiesManagerClient entitiesManager;

    private MerchantsManagerClient() {
        this.profilesManager = new MerchantProfilesManagerClient();
        this.entitiesManager = new EntitiesManagerClient();
    }

    public static void create() {
        if (instance == null)
            instance = new MerchantsManagerClient();
    }

    public static MerchantsManagerClient instance() {
        return instance;
    }

    public MerchantProfilesManagerClient getMerchantProfilesManager() {
        return this.profilesManager;
    }

    public EntitiesManagerClient getEntitiesManager() {
        return this.entitiesManager;
    }

    public void openManagementMenu() {
        ClientReference.delegateToClientThread(()->ClientReference.displayGuiScreen(new ManagementMenuGUIScreen()));
    }

    public void openMerchantMenuSynced(int entityId) {
        BetterMerchantsMain.network().sendToServer(new SPOpenMerchantMenu(entityId));
    }

    public void openMerchantMenu(long profileId) {
        ClientReference.delegateToClientThread(()->ClientReference.displayGuiScreen(new MerchantMenuGUIScreen(this.entitiesManager.getProfile(profileId))));
    }

    public void performBuySynced(long profileId, long offerId) {
        BetterMerchantsMain.network().sendToServer(new SPMerchantOperation(OperationsProcessor.EnumOperation.BUY, profileId, offerId));
    }

    public void performSellingSynced(long profileId, long offerId) {
        BetterMerchantsMain.network().sendToServer(new SPMerchantOperation(OperationsProcessor.EnumOperation.SELLING, profileId, offerId));
    }

    public void updateMerchantMenu(OperationsProcessor.EnumOperation operation) {
        if (ClientReference.hasActiveGUI() && ClientReference.getCurrentScreen() instanceof MerchantMenuGUIScreen) {
            switch (operation) {
            case BUY:
                ((MerchantMenuGUIScreen) ClientReference.getCurrentScreen()).getBuySection().bought();
                break;
            case SELLING:
                ((MerchantMenuGUIScreen) ClientReference.getCurrentScreen()).getSellingSection().sold();
                break;
            }
        }
    }

    public void load() {
        MerchantsLoader.loadPersistentData(this.profilesManager);
        MerchantsLoader.loadPersistentData(this.entitiesManager);
    }

    public long getWorldId() {
        return this.worldId;
    }

    public void setWorldId(long worldId) {
        this.worldId = worldId;
        this.worldFolder = CommonReference.getGameFolder() + "/better merchants/client/" + worldId + "/";
        this.load();
    }

    public String getWorldFolder() {
        return this.worldFolder;
    }

    public void reset() {
        this.profilesManager.reset();
        this.entitiesManager.reset();
    }
}
