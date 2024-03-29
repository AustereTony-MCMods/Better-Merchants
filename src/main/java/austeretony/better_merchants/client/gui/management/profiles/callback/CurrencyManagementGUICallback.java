package austeretony.better_merchants.client.gui.management.profiles.callback;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.button.GUICheckBoxButton;
import austeretony.alternateui.screen.button.GUISlider;
import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.panel.GUIButtonPanel;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.alternateui.util.EnumGUIOrientation;
import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.gui.management.ManagementMenuGUIScreen;
import austeretony.better_merchants.client.gui.management.ProfilesManagementGUISection;
import austeretony.better_merchants.client.gui.management.profiles.InventoryItemGUIButton;
import austeretony.better_merchants.client.gui.settings.GUISettings;
import austeretony.better_merchants.common.main.SoundEffects;
import net.minecraft.item.ItemStack;

public class CurrencyManagementGUICallback extends AbstractGUICallback {

    private final ManagementMenuGUIScreen screen;

    private final ProfilesManagementGUISection section; 

    private GUICheckBoxButton useCurrencyButton, useItemButton;

    private GUIButton confirmButton, cancelButton;

    private GUIButtonPanel itemsPanel;

    private InventoryItemGUIButton currentButton;

    public CurrencyManagementGUICallback(ManagementMenuGUIScreen screen, ProfilesManagementGUISection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;   
        this.section = section;
    }

    @Override
    public void init() {
        this.addElement(new CurrencyManagementCallbackGUIFiller(0, 0, this.getWidth(), this.getHeight()));//main background 1st layer

        this.addElement(new GUITextLabel(2, 2).setDisplayText(ClientReference.localize("better_merchants.gui.management.currencyManagementCallback"), true, GUISettings.instance().getTitleScale()));

        this.addElement(this.useCurrencyButton = new GUICheckBoxButton(4, 14, 6).setSound(SoundEffects.BUTTON_CLICK.soundEvent)
                .enableDynamicBackground(GUISettings.instance().getEnabledButtonColor(), GUISettings.instance().getDisabledButtonColor(), GUISettings.instance().getHoveredButtonColor())
                .toggle());
        this.addElement(this.useItemButton = new GUICheckBoxButton(4, 24, 6).setSound(SoundEffects.BUTTON_CLICK.soundEvent)
                .enableDynamicBackground(GUISettings.instance().getEnabledButtonColor(), GUISettings.instance().getDisabledButtonColor(), GUISettings.instance().getHoveredButtonColor()));

        this.addElement(new GUITextLabel(14, 13).setDisplayText(ClientReference.localize("better_merchants.gui.management.useCurrency"), false, GUISettings.instance().getSubTextScale()));
        this.addElement(new GUITextLabel(14, 23).setDisplayText(ClientReference.localize("better_merchants.gui.management.useItem"), false, GUISettings.instance().getSubTextScale()));

        this.itemsPanel = new GUIButtonPanel(EnumGUIOrientation.VERTICAL, 0, 33, 137, 16).setButtonsOffset(1).setTextScale(GUISettings.instance().getTextScale());
        this.addElement(this.itemsPanel);       
        GUIScroller scroller = new GUIScroller(27, 5);
        this.itemsPanel.initScroller(scroller);
        GUISlider slider = new GUISlider(this.getX() + 138, this.getY() + 33, 2, 84);
        slider.setDynamicBackgroundColor(GUISettings.instance().getEnabledSliderColor(), GUISettings.instance().getDisabledSliderColor(), GUISettings.instance().getHoveredSliderColor());
        scroller.initSlider(slider);

        this.loadItems();

        this.addElement(this.confirmButton = new GUIButton(15, this.getHeight() - 12, 40, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent).enableDynamicBackground().setDisplayText(ClientReference.localize("better_merchants.gui.confirmButton"), true, GUISettings.instance().getButtonTextScale()));
        this.addElement(this.cancelButton = new GUIButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent).enableDynamicBackground().setDisplayText(ClientReference.localize("better_merchants.gui.cancelButton"), true, GUISettings.instance().getButtonTextScale()));
    }

    @Override
    protected void onOpen() {
        this.useCurrencyButton.toggle();
        this.useItemButton.setToggled(false);
        if (this.currentButton != null) {
            this.currentButton.setToggled(false);
            this.currentButton = null;
        }
        this.loadItems();
    }

    private void loadItems() {
        this.itemsPanel.reset();
        this.itemsPanel.getScroller().resetPosition();
        this.itemsPanel.getScroller().getSlider().reset();

        InventoryItemGUIButton button;
        for (ItemStack itemStack : ClientReference.getClientPlayer().inventory.mainInventory) {
            if (!itemStack.isEmpty()) {
                button = new InventoryItemGUIButton(itemStack);
                button.enableDynamicBackground(GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getHoveredElementColor());
                button.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
                this.itemsPanel.addButton(button);
            }              
        }
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0) { 
            if (element == this.cancelButton)
                this.close();
            else if (element == this.confirmButton) {
                if (this.useCurrencyButton.isToggled())
                    this.section.setProfileUseCurrency();
                else if (this.useItemButton.isToggled() && this.currentButton != null)
                    this.section.setProfileUseItem(this.currentButton.getItemStack());
                this.close();
            } else if (element == this.useCurrencyButton) {
                if (this.useCurrencyButton.isToggled()) {
                    this.useItemButton.setToggled(false);
                    if (this.currentButton != null) {
                        this.currentButton.setToggled(false);
                        this.currentButton = null;
                    }
                }
            } else if (element == this.useItemButton) {
                if (this.useItemButton.isToggled())
                    this.useCurrencyButton.setToggled(false);
            } else if (element instanceof InventoryItemGUIButton) {
                InventoryItemGUIButton button = (InventoryItemGUIButton) element;
                if (this.currentButton != button) {
                    if (this.currentButton != null)
                        this.currentButton.setToggled(false);
                    button.toggle();                    
                    this.currentButton = button;

                    this.useCurrencyButton.setToggled(false);
                    this.useItemButton.toggle();
                }
            }
        }
    }
}
