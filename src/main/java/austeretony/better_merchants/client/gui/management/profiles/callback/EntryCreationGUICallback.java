package austeretony.better_merchants.client.gui.management.profiles.callback;

import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.text.GUITextField;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.client.gui.management.ManagementMenuGUIScreen;
import austeretony.better_merchants.client.gui.management.ProfilesManagementGUISection;
import austeretony.better_merchants.client.gui.settings.GUISettings;
import austeretony.better_merchants.common.main.EntityEntry;
import austeretony.better_merchants.common.main.SoundEffects;

public class EntryCreationGUICallback extends AbstractGUICallback {

    private final ManagementMenuGUIScreen screen;

    private final ProfilesManagementGUISection section;

    private GUITextField nameField, professionField;

    private GUIButton confirmButton, cancelButton;

    public EntryCreationGUICallback(ManagementMenuGUIScreen screen, ProfilesManagementGUISection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    public void init() {
        this.addElement(new EntryCreationCallbackGUIFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new GUITextLabel(2, 2).setDisplayText(ClientReference.localize("better_merchants.gui.management.merchantCreationCallback"), true, GUISettings.instance().getTitleScale())); 
        this.addElement(new GUITextLabel(2, 16).setDisplayText(ClientReference.localize("better_merchants.gui.management.name"), false, GUISettings.instance().getSubTextScale())); 
        this.addElement(new GUITextLabel(2, 36).setDisplayText(ClientReference.localize("better_merchants.gui.management.profession"), false, GUISettings.instance().getSubTextScale()));    

        this.addElement(this.nameField = new GUITextField(2, 25, 136, 9, EntityEntry.MAX_NAME_LENGTH).setTextScale(GUISettings.instance().getSubTextScale()).setLineOffset(3)
                .enableDynamicBackground(GUISettings.instance().getEnabledTextFieldColor(), GUISettings.instance().getDisabledTextFieldColor(), GUISettings.instance().getHoveredTextFieldColor())
                .cancelDraggedElementLogic());
        this.addElement(this.professionField = new GUITextField(2, 45, 136, 9, EntityEntry.MAX_PROFESSION_LENGTH).setTextScale(GUISettings.instance().getSubTextScale()).setLineOffset(3)
                .enableDynamicBackground(GUISettings.instance().getEnabledTextFieldColor(), GUISettings.instance().getDisabledTextFieldColor(), GUISettings.instance().getHoveredTextFieldColor())
                .cancelDraggedElementLogic());

        this.addElement(this.confirmButton = new GUIButton(15, this.getHeight() - 12, 40, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent).enableDynamicBackground().setDisplayText(ClientReference.localize("better_merchants.gui.confirmButton"), true, GUISettings.instance().getButtonTextScale()));
        this.addElement(this.cancelButton = new GUIButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10).setSound(SoundEffects.BUTTON_CLICK.soundEvent).enableDynamicBackground().setDisplayText(ClientReference.localize("better_merchants.gui.cancelButton"), true, GUISettings.instance().getButtonTextScale()));
    }

    @Override
    protected void onOpen() {
        this.nameField.reset();
        this.professionField.reset();
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0) { 
            if (element == this.cancelButton)
                this.close();
            else if (element == this.confirmButton) {
                String 
                name = this.nameField.getTypedText().isEmpty() ? this.section.pointedEntity.getName() : this.nameField.getTypedText(),
                        profession = this.professionField.getTypedText().isEmpty() ? "" : this.professionField.getTypedText();
                MerchantsManagerClient.instance().getEntitiesManager().createMerchantSynced(this.section.pointedEntity, name, profession, this.section.getCurrentProfileChangesBuffer().getId());
                this.close();
            }
        }
    }
}
