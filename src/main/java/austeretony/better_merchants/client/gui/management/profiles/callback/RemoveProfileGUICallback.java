package austeretony.better_merchants.client.gui.management.profiles.callback;

import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.client.gui.management.ManagementMenuGUIScreen;
import austeretony.better_merchants.client.gui.management.ProfilesManagementGUISection;
import austeretony.better_merchants.client.gui.settings.GUISettings;
import austeretony.better_merchants.common.main.SoundEffects;

public class RemoveProfileGUICallback extends AbstractGUICallback {

    private final ManagementMenuGUIScreen screen;

    private final ProfilesManagementGUISection section;

    private GUITextLabel requestLabel;

    private GUIButton confirmButton, cancelButton;

    public RemoveProfileGUICallback(ManagementMenuGUIScreen screen, ProfilesManagementGUISection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    public void init() {
        this.addElement(new RemoveProfileCallbackGUIFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new GUITextLabel(2, 2).setDisplayText(ClientReference.localize("better_merchants.gui.management.removeProfileCallback"), true, GUISettings.instance().getTitleScale()));

        this.addElement(this.requestLabel = new GUITextLabel(2, 16));     

        this.addElement(this.confirmButton = new GUIButton(15, this.getHeight() - 12, 40, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent).enableDynamicBackground().setDisplayText(ClientReference.localize("better_merchants.gui.confirmButton"), true, GUISettings.instance().getButtonTextScale()));
        this.addElement(this.cancelButton = new GUIButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent).enableDynamicBackground().setDisplayText(ClientReference.localize("better_merchants.gui.cancelButton"), true, GUISettings.instance().getButtonTextScale()));
    }

    @Override   
    protected void onOpen() {
        this.requestLabel.setDisplayText(ClientReference.localize("better_merchants.gui.management.removeProfileCallback.request", this.section.getCurrentProfileChangesBuffer().getName()), false, GUISettings.instance().getTextScale());
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0) { 
            if (element == this.cancelButton)
                this.close();
            else if (element == this.confirmButton) {
                this.section.resetProfileData();
                MerchantsManagerClient.instance().getMerchantProfilesManager().removeProfileSynced(this.section.getCurrentProfileChangesBuffer().getId());
                this.section.sortProfiles(0);
                this.close();
            }
        }
    }
}
