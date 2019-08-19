package austeretony.better_merchants.client.gui.management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.button.GUISlider;
import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.contextmenu.GUIContextMenu;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.panel.GUIButtonPanel;
import austeretony.alternateui.screen.text.GUITextField;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.alternateui.util.EnumGUIOrientation;
import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.client.gui.IndexedGUIButton;
import austeretony.better_merchants.client.gui.MerchantsGUITextures;
import austeretony.better_merchants.client.gui.management.profiles.GUICurrency;
import austeretony.better_merchants.client.gui.management.profiles.OfferFullGUIButton;
import austeretony.better_merchants.client.gui.management.profiles.ProfilesSectionGUIFiller;
import austeretony.better_merchants.client.gui.management.profiles.callback.CurrencyManagementGUICallback;
import austeretony.better_merchants.client.gui.management.profiles.callback.EntryCreationGUICallback;
import austeretony.better_merchants.client.gui.management.profiles.callback.OfferCreationGUICallback;
import austeretony.better_merchants.client.gui.management.profiles.callback.OfferEditingGUICallback;
import austeretony.better_merchants.client.gui.management.profiles.callback.ProfileCreationGUICallback;
import austeretony.better_merchants.client.gui.management.profiles.callback.ProfileNameEditGUICallback;
import austeretony.better_merchants.client.gui.management.profiles.callback.RemoveProfileGUICallback;
import austeretony.better_merchants.client.gui.management.profiles.callback.SaveChangesGUICallback;
import austeretony.better_merchants.client.gui.management.profiles.context.EditOfferContextAction;
import austeretony.better_merchants.client.gui.management.profiles.context.EditProfileCurrencyContextAction;
import austeretony.better_merchants.client.gui.management.profiles.context.EditProfileNameContextAction;
import austeretony.better_merchants.client.gui.management.profiles.context.OfferCreationContextAction;
import austeretony.better_merchants.client.gui.management.profiles.context.RemoveOfferContextAction;
import austeretony.better_merchants.client.gui.management.profiles.context.RemoveProfileContextAction;
import austeretony.better_merchants.client.gui.merchant.MerchantMenuGUIScreen;
import austeretony.better_merchants.client.gui.settings.GUISettings;
import austeretony.better_merchants.common.main.MerchantOffer;
import austeretony.better_merchants.common.main.MerchantProfile;
import austeretony.better_merchants.common.main.SoundEffects;
import austeretony.better_merchants.common.util.ItemStackWrapper;
import austeretony.better_merchants.common.util.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;

public class ProfilesManagementGUISection extends AbstractGUISection {

    private final ManagementMenuGUIScreen screen;

    private GUIButton searchButton, refreshButton, sortUpButton, sortDownButton, createButton;

    private GUIButtonPanel profilesPanel;

    private GUITextLabel profilesAmountTextLabel;

    private GUITextField searchField;

    private AbstractGUICallback profileCreationCallback, removeProfileCallback, profileNameEditingCallback, 
    profileCurrencyManagementCallback, offerCreationCallback, offerEditingCallback, saveChangesCallback, entryCreationCallback;

    private IndexedGUIButton currentProfileButton;

    //Profile Element

    private final static Map<Long, ItemStack> ITEMSTACK_CACHE = new HashMap<Long, ItemStack>();

    private GUITextLabel profileNameTextLabel;

    private GUIButton profileSaveChangesButton, profileOpenButton, createMerchantButton;

    private GUICurrency profileCurrencyElement;

    private GUIButtonPanel profileOffersPanel;

    private MerchantProfile changesBuffer;

    private OfferFullGUIButton currentOfferButton;

    public final Entity pointedEntity;

    public ProfilesManagementGUISection(ManagementMenuGUIScreen screen) {
        super(screen);
        this.screen = screen;
        this.pointedEntity = ClientReference.getPointedEntity();
    }

    @Override
    public void init() {
        this.addElement(new ProfilesSectionGUIFiller(0, 0, this.getWidth(), this.getHeight()));
        String title = ClientReference.localize("better_merchants.gui.management.profiles.title");
        this.addElement(new GUITextLabel(2, 4).setDisplayText(title, false, GUISettings.instance().getTitleScale()));

        this.addElement(this.searchButton = new GUIButton(7, 15, 7, 7).setSound(SoundEffects.BUTTON_CLICK.soundEvent).setTexture(MerchantsGUITextures.SEARCH_ICONS, 7, 7));   
        this.addElement(this.sortDownButton = new GUIButton(2, 19, 3, 3).setSound(SoundEffects.BUTTON_CLICK.soundEvent).setTexture(MerchantsGUITextures.SORT_DOWN_ICONS, 3, 3)); 
        this.addElement(this.sortUpButton = new GUIButton(2, 15, 3, 3).setSound(SoundEffects.BUTTON_CLICK.soundEvent).setTexture(MerchantsGUITextures.SORT_UP_ICONS, 3, 3)); 
        this.addElement(this.refreshButton = new GUIButton(0, 14, 10, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent).setTexture(MerchantsGUITextures.REFRESH_ICONS, 9, 9));
        this.addElement(this.profilesAmountTextLabel = new GUITextLabel(0, 15).setTextScale(GUISettings.instance().getSubTextScale()));   

        this.profilesPanel = new GUIButtonPanel(EnumGUIOrientation.VERTICAL, 0, 24, 82, 10).setButtonsOffset(1).setTextScale(GUISettings.instance().getPanelTextScale());
        this.addElement(this.profilesPanel);
        this.addElement(this.searchField = new GUITextField(0, 14, 85, 9, MerchantProfile.MAX_PROFILE_NAME_LENGTH)
                .enableDynamicBackground(GUISettings.instance().getEnabledTextFieldColor(), GUISettings.instance().getDisabledTextFieldColor(), GUISettings.instance().getHoveredTextFieldColor())
                .setDisplayText("...", false, GUISettings.instance().getSubTextScale()).setLineOffset(3).cancelDraggedElementLogic().disableFull());
        this.profilesPanel.initSearchField(this.searchField);
        GUIScroller scroller = new GUIScroller(MathUtils.clamp(MerchantsManagerClient.instance().getMerchantProfilesManager().getProfilesAmount(), 10, 100), 10);
        this.profilesPanel.initScroller(scroller);
        GUISlider slider = new GUISlider(83, 24, 2, 109);
        slider.setDynamicBackgroundColor(GUISettings.instance().getEnabledSliderColor(), GUISettings.instance().getDisabledSliderColor(), GUISettings.instance().getHoveredSliderColor());
        scroller.initSlider(slider);    

        this.addElement(this.createButton = new GUIButton(22, 137,  40, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent)
                .enableDynamicBackground(GUISettings.instance().getEnabledButtonColor(), GUISettings.instance().getDisabledButtonColor(), GUISettings.instance().getHoveredButtonColor())
                .setDisplayText(ClientReference.localize("better_merchants.gui.management.create"), true, GUISettings.instance().getButtonTextScale())); 

        GUIContextMenu menu = new GUIContextMenu(GUISettings.instance().getContextMenuWidth(), 10).setScale(GUISettings.instance().getContextMenuScale()).setTextScale(GUISettings.instance().getTextScale()).setTextAlignment(EnumGUIAlignment.LEFT, 2);
        menu.setOpenSound(SoundEffects.CONTEXT_OPEN.soundEvent);
        menu.setCloseSound(SoundEffects.CONTEXT_CLOSE.soundEvent);
        this.profilesPanel.initContextMenu(menu);
        menu.enableDynamicBackground(GUISettings.instance().getEnabledContextActionColor(), GUISettings.instance().getDisabledContextActionColor(), GUISettings.instance().getHoveredContextActionColor());
        menu.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
        menu.addElement(new EditProfileNameContextAction(this));
        menu.addElement(new EditProfileCurrencyContextAction(this));
        menu.addElement(new OfferCreationContextAction(this));
        menu.addElement(new RemoveProfileContextAction(this));

        this.profileCreationCallback = new ProfileCreationGUICallback(this.screen, this, 140, 50).enableDefaultBackground();
        this.removeProfileCallback = new RemoveProfileGUICallback(this.screen, this, 140, 50).enableDefaultBackground();
        this.profileNameEditingCallback = new ProfileNameEditGUICallback(this.screen, this, 140, 50).enableDefaultBackground();
        this.profileCurrencyManagementCallback = new CurrencyManagementGUICallback(this.screen, this, 140, 132).enableDefaultBackground();
        this.offerCreationCallback = new OfferCreationGUICallback(this.screen, this, 140, 184);
        this.offerEditingCallback = new OfferEditingGUICallback(this.screen, this, 140, 184);

        this.saveChangesCallback = new SaveChangesGUICallback(this.screen, this, 140, 40).enableDefaultBackground();

        this.entryCreationCallback = new EntryCreationGUICallback(this.screen, this, 140, 72).enableDefaultBackground();

        this.initProfileElements();

        ManagementMenuGUIScreen.screenInitialized = true;
    }

    private void initProfileElements() {
        this.addElement(this.profileNameTextLabel = new GUITextLabel(90, 19).setTextScale(GUISettings.instance().getTitleScale()).disableFull());      

        this.addElement(this.profileCurrencyElement = new GUICurrency(90, 21));
        this.profileCurrencyElement.disable();

        this.addElement(this.profileSaveChangesButton = new GUIButton(90, 137, 40, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent)
                .enableDynamicBackground(GUISettings.instance().getEnabledButtonColor(), GUISettings.instance().getDisabledButtonColor(), GUISettings.instance().getHoveredButtonColor())
                .setDisplayText(ClientReference.localize("better_merchants.gui.management.saveChangesButton"), true, GUISettings.instance().getButtonTextScale()).disableFull());

        this.addElement(this.profileOpenButton = new GUIButton(134, 137, 40, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent)
                .enableDynamicBackground(GUISettings.instance().getEnabledButtonColor(), GUISettings.instance().getDisabledButtonColor(), GUISettings.instance().getHoveredButtonColor())
                .setDisplayText(ClientReference.localize("better_merchants.gui.management.openProfileButton"), true, GUISettings.instance().getButtonTextScale()).disableFull());

        this.addElement(this.createMerchantButton = new GUIButton(178, 137, 40, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent)
                .enableDynamicBackground(GUISettings.instance().getEnabledButtonColor(), GUISettings.instance().getDisabledButtonColor(), GUISettings.instance().getHoveredButtonColor())
                .setDisplayText(ClientReference.localize("better_merchants.gui.management.write"), true, GUISettings.instance().getButtonTextScale()).disableFull()); 

        this.profileOffersPanel = new GUIButtonPanel(EnumGUIOrientation.VERTICAL, 86, 32, 200, 16).setButtonsOffset(1).setTextScale(GUISettings.instance().getTextScale()).disableFull();
        this.addElement(this.profileOffersPanel);
        GUIScroller scroller = new GUIScroller(100, 6);
        this.profileOffersPanel.initScroller(scroller);
        GUISlider slider = new GUISlider(287, 32, 2, 101).disableFull();
        slider.setDynamicBackgroundColor(GUISettings.instance().getEnabledSliderColor(), GUISettings.instance().getDisabledSliderColor(), GUISettings.instance().getHoveredSliderColor());
        scroller.initSlider(slider);    

        GUIContextMenu menu = new GUIContextMenu(GUISettings.instance().getContextMenuWidth() + 15, 10).setScale(GUISettings.instance().getContextMenuScale()).setTextScale(GUISettings.instance().getTextScale()).setTextAlignment(EnumGUIAlignment.LEFT, 2);
        menu.setOpenSound(SoundEffects.CONTEXT_OPEN.soundEvent);
        menu.setCloseSound(SoundEffects.CONTEXT_CLOSE.soundEvent);
        this.profileOffersPanel.initContextMenu(menu);
        menu.enableDynamicBackground(GUISettings.instance().getEnabledContextActionColor(), GUISettings.instance().getDisabledContextActionColor(), GUISettings.instance().getHoveredContextActionColor());
        menu.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
        menu.addElement(new EditOfferContextAction(this));
        menu.addElement(new RemoveOfferContextAction(this));
    }

    public void sortProfiles(int mode) {
        List<MerchantProfile> profiles = new ArrayList<MerchantProfile>(MerchantsManagerClient.instance().getMerchantProfilesManager().getProfiles());

        if (mode == 0)
            Collections.sort(profiles, (p1, p2)->p1.getName().compareTo(p2.getName()));
        else
            Collections.sort(profiles, (p1, p2)->p2.getName().compareTo(p1.getName()));

        this.profilesPanel.reset();
        IndexedGUIButton<Long> button;
        for (MerchantProfile profile : profiles) {
            button = new IndexedGUIButton(profile.getId());
            button.enableDynamicBackground(GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getHoveredElementColor());
            button.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
            button.setDisplayText(profile.getName());
            button.setTextAlignment(EnumGUIAlignment.LEFT, 1);

            this.profilesPanel.addButton(button);
        }

        this.profilesAmountTextLabel.setDisplayText(String.valueOf(MerchantsManagerClient.instance().getMerchantProfilesManager().getProfilesAmount()));     
        this.profilesAmountTextLabel.setX(83 - this.textWidth(this.profilesAmountTextLabel.getDisplayText(), GUISettings.instance().getSubTextScale()));
        this.refreshButton.setX(this.profilesAmountTextLabel.getX() - 11);

        this.searchField.reset();

        this.profilesPanel.getScroller().resetPosition();
        this.profilesPanel.getScroller().getSlider().reset();

        this.sortUpButton.toggle();
        this.sortDownButton.setToggled(false);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.searchField.isEnabled() && !this.searchField.isHovered()) {
            this.sortUpButton.enableFull();
            this.sortDownButton.enableFull();
            this.searchButton.enableFull();
            this.refreshButton.enableFull();
            this.searchField.disableFull();
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);                 
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0) { 
            if (element == this.createButton)
                this.profileCreationCallback.open();
            else if (element == this.searchButton) {
                this.searchField.enableFull();
                this.sortUpButton.disableFull();
                this.sortDownButton.disableFull();
                this.searchButton.disableFull();
                this.refreshButton.disableFull();
            } else if (element == this.sortDownButton) {
                if (!this.sortDownButton.isToggled()) {
                    this.sortProfiles(1);
                    this.sortUpButton.setToggled(false);
                    this.sortDownButton.toggle(); 
                }
            } else if (element == this.sortUpButton) {
                if (!this.sortUpButton.isToggled()) {
                    this.sortProfiles(0);
                    this.sortDownButton.setToggled(false);
                    this.sortUpButton.toggle();
                }
            } else if (element == this.refreshButton) {
                this.sortProfiles(0);
                this.resetProfileData();
            } else if (element == this.profileSaveChangesButton)
                this.openSaveChangesCallback();
            else if (element == this.profileOpenButton) 
                ClientReference.displayGuiScreen(
                        new MerchantMenuGUIScreen(MerchantsManagerClient.instance().getMerchantProfilesManager().getProfile(this.changesBuffer.getId())));
            else if (element == this.createMerchantButton)
                this.entryCreationCallback.open();

        }
        if (element instanceof OfferFullGUIButton) {
            OfferFullGUIButton button = (OfferFullGUIButton) element;
            if (this.currentOfferButton != button)                
                this.currentOfferButton = button;
        } else if (element instanceof IndexedGUIButton) {
            IndexedGUIButton<Long> button = (IndexedGUIButton) element;
            if (this.currentProfileButton != button) {
                if (this.currentProfileButton != null)
                    this.currentProfileButton.setToggled(false);
                button.toggle();                    
                this.currentProfileButton = button;
                this.initProfileData(button.index);
            }
        }
    }

    public void initProfileData(long profileId) {
        this.changesBuffer = MerchantsManagerClient.instance().getMerchantProfilesManager().getProfile(profileId).copy();

        this.profileNameTextLabel.enableFull();

        if (this.changesBuffer.isUsingCurrency()) {
            this.profileCurrencyElement.setUseCurrency();
        } else {
            ItemStack itemStack;
            if (ITEMSTACK_CACHE.containsKey(profileId)) {
                itemStack = ITEMSTACK_CACHE.get(profileId);
                this.profileCurrencyElement.setUseItem(itemStack);
            } else {    
                itemStack = this.changesBuffer.getCurrencyStack().getItemStack();
                ITEMSTACK_CACHE.put(profileId, itemStack);   
                this.profileCurrencyElement.setUseItem(itemStack);
            }
        }
        this.profileCurrencyElement.enable();

        this.loadOffers(this.changesBuffer);
        this.profileOffersPanel.enableFull();
        this.profileOffersPanel.getScroller().getSlider().enableFull();

        this.profileSaveChangesButton.enableFull();
        this.profileOpenButton.enableFull();
        this.createMerchantButton.enableFull();

        if (this.pointedEntity == null || !(this.pointedEntity instanceof EntityLiving)) 
            this.createMerchantButton.disable();

        this.profileNameTextLabel.setDisplayText(this.changesBuffer.getName());
        this.profileCurrencyElement.setX(this.profileNameTextLabel.getX() + this.textWidth(this.changesBuffer.getName(), GUISettings.instance().getTitleScale()) + 4);
    }

    private void loadOffers(MerchantProfile profile) {
        List<MerchantOffer> offers = new ArrayList<MerchantOffer>(profile.getOffers());

        Collections.sort(offers, (o1, o2)->(int) ((o1.offerId - o2.offerId) / 5_000L));

        this.profileOffersPanel.reset();
        OfferFullGUIButton button;
        ItemStack currencyItemStack = null;
        if (!profile.isUsingCurrency()) {
            if (ITEMSTACK_CACHE.containsKey(profile.getId()))
                currencyItemStack = ITEMSTACK_CACHE.get(profile.getId());
            else
                ITEMSTACK_CACHE.put(profile.getId(), currencyItemStack = this.changesBuffer.getCurrencyStack().getItemStack());  
        }

        for (MerchantOffer offer : offers) {
            button = new OfferFullGUIButton(offer.offerId, offer.getOfferedStack().getItemStack(), offer.getAmount(), 
                    offer.isSellingOnly() ? 0 : offer.getBuyCost(), offer.isSellingEnabled() ? offer.getSellingCost() : 0, currencyItemStack);
            button.enableDynamicBackground(GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getHoveredElementColor());
            button.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
            this.profileOffersPanel.addButton(button);
        }

        this.profileOffersPanel.getScroller().resetPosition();
        this.profileOffersPanel.getScroller().getSlider().reset();
    }

    public void resetProfileData() {
        this.profileNameTextLabel.disableFull();

        this.profileCurrencyElement.disable();

        this.profileOffersPanel.disableFull();
        this.profileOffersPanel.getScroller().getSlider().disableFull();

        this.profileSaveChangesButton.disableFull();
        this.profileOpenButton.disableFull();
        this.createMerchantButton.disableFull();
    }

    public MerchantProfile getCurrentProfileChangesBuffer() {
        return this.changesBuffer;
    }

    public OfferFullGUIButton getCurrentOfferButton() {
        return this.currentOfferButton;
    }

    public void updateProfileName(String name) {       
        this.changesBuffer.setName(name);
        this.profileNameTextLabel.setDisplayText(name);
    }

    public void setProfileUseCurrency() {
        this.changesBuffer.setUseCurrency(true);
        this.profileCurrencyElement.setUseCurrency();
        this.loadOffers(this.changesBuffer);
    }

    public void setProfileUseItem(ItemStack itemStack) {
        this.changesBuffer.setUseCurrency(false);
        this.changesBuffer.setCurrencyStack(ItemStackWrapper.getFromStack(itemStack));
        this.profileCurrencyElement.setUseItem(itemStack);
        ITEMSTACK_CACHE.remove(this.changesBuffer.getId());
        this.loadOffers(this.changesBuffer);
    }

    public void addOfferToCurrentProfile(MerchantOffer offer) {
        this.changesBuffer.addOffer(offer);
        this.loadOffers(this.changesBuffer);
    }

    public void removeOfferFromCurrentProfile(long offerId) {
        this.changesBuffer.removeOffer(offerId);
        this.loadOffers(this.changesBuffer);
    }

    public IndexedGUIButton<Long> getCurrentProfileButton() {
        return this.currentProfileButton;
    }

    public void openRemoveProfileCallback() {
        this.removeProfileCallback.open();
    }

    public void openProfileNameEditingCallback() {
        this.profileNameEditingCallback.open();
    }

    public void openProfileCurrencyManagementCallback() {
        this.profileCurrencyManagementCallback.open();
    }

    public void openOfferCreationCallback() {
        this.offerCreationCallback.open();
    }

    public void openOfferEditingCallback() {
        this.offerEditingCallback.open();
    }

    public void openSaveChangesCallback() {
        this.saveChangesCallback.open();
    }

    public GUIButtonPanel getOffersPanel() {
        return this.profileOffersPanel;
    }
}
