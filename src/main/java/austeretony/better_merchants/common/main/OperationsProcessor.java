package austeretony.better_merchants.common.main;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import austeretony.better_merchants.common.CommonReference;
import austeretony.better_merchants.common.MerchantsManagerServer;
import austeretony.better_merchants.common.network.client.CPUpdateMerchantMenu;
import austeretony.better_merchants.common.util.InventoryHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import the_fireplace.grandeconomy.api.GrandEconomyApi;

public class OperationsProcessor {

    private final UUID playerUUID;

    private final Queue<MerchantOperation> operations = new ConcurrentLinkedQueue<MerchantOperation>();

    public OperationsProcessor(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void addOperation(EnumOperation operation, long profileId, long offerId) {
        this.operations.offer(new MerchantOperation(operation, profileId, offerId));
    }

    public void process() {
        MerchantOperation operation;
        MerchantProfile profile = null;
        MerchantOffer offer;
        EntityPlayerMP playerMP = null;
        boolean save = false;
        while (!this.operations.isEmpty()) {
            operation = this.operations.poll();
            if (profile == null) {
                if (MerchantsManagerServer.instance().getEntitiesManager().profileExist(operation.profileId))
                    profile = MerchantsManagerServer.instance().getEntitiesManager().getProfile(operation.profileId);
                else
                    break;
            } 
            if (profile != null) {
                if (profile.offerExist(operation.offerId)) {
                    playerMP = CommonReference.playerByUUID(this.playerUUID);
                    offer = profile.getOffer(operation.offerId);
                    switch (operation.operation) {
                    case BUY:
                        if (!offer.isSellingOnly())
                            save = this.buy(playerMP, profile, offer);
                        break;
                    case SELLING:
                        if (offer.isSellingEnabled())
                            save = this.sell(playerMP, profile, offer);
                        break;
                    }
                }
            }
        }   
        if (save)
            CurrencyHandler.updateBalance(playerMP);
    }

    private boolean buy(EntityPlayerMP playerMP, MerchantProfile profile, MerchantOffer offer) {
        boolean save = false;
        if (!InventoryHelper.haveEnoughSpace(playerMP, offer.getAmount()))
            return false;

        if (profile.isUsingCurrency()) {
            if (GrandEconomyApi.getBalance(this.playerUUID) < offer.getBuyCost())
                return false;

            GrandEconomyApi.takeFromBalance(this.playerUUID, offer.getBuyCost(), false);
            save = true;
        } else {
            if (InventoryHelper.getEqualStackAmount(playerMP, profile.getCurrencyStack()) < offer.getBuyCost())
                return false;

            InventoryHelper.removeEqualStack(playerMP, profile.getCurrencyStack(), offer.getBuyCost());
        }

        InventoryHelper.addItemStack(playerMP, offer.getOfferedStack().getItemStack(), offer.getAmount());
        this.notifyPlayer(playerMP, EnumOperation.BUY);

        return save;
    }

    private boolean sell(EntityPlayerMP playerMP, MerchantProfile profile, MerchantOffer offer) {
        boolean save = false;

        if (!profile.isUsingCurrency() && !InventoryHelper.haveEnoughSpace(playerMP, offer.getSellingCost()))
            return false;

        if (InventoryHelper.getEqualStackAmount(playerMP, offer.getOfferedStack()) < offer.getAmount())
            return false;

        if (profile.isUsingCurrency()) {
            GrandEconomyApi.addToBalance(this.playerUUID, offer.getSellingCost(), false);
            save = true;
        } else
            InventoryHelper.addItemStack(playerMP, profile.getCurrencyStack().getItemStack(), offer.getSellingCost());

        InventoryHelper.removeEqualStack(playerMP, offer.getOfferedStack(), offer.getAmount());
        this.notifyPlayer(playerMP, EnumOperation.SELLING);

        return save;
    }

    public void notifyPlayer(EntityPlayerMP playerMP, EnumOperation operation) {
        BetterMerchantsMain.network().sendTo(new CPUpdateMerchantMenu(operation), playerMP); 
    }

    public static class MerchantOperation {

        public final EnumOperation operation;

        public final long profileId, offerId;

        public MerchantOperation(EnumOperation operation, long profileId, long offerId) {
            this.operation = operation;
            this.profileId = profileId;
            this.offerId = offerId;
        }
    }

    public enum EnumOperation {

        BUY,
        SELLING;
    }
}
