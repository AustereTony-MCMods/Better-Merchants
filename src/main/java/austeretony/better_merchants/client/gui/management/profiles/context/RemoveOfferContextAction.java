package austeretony.better_merchants.client.gui.management.profiles.context;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.gui.management.ProfilesManagementGUISection;
import austeretony.better_merchants.client.gui.management.profiles.OfferFullGUIButton;

public class RemoveOfferContextAction extends AbstractContextAction {

    private final ProfilesManagementGUISection section;

    private final String name;

    public RemoveOfferContextAction(ProfilesManagementGUISection section) {
        this.section = section;
        this.name = ClientReference.localize("better_merchants.gui.management.remove");
    }

    @Override
    protected String getName(GUIBaseElement currElement) {
        return this.name;
    }

    @Override
    protected boolean isValid(GUIBaseElement currElement) {
        return true;
    }

    @Override
    protected void execute(GUIBaseElement currElement) {
        this.section.removeOfferFromCurrentProfile(((OfferFullGUIButton) currElement).index); 
    }
}
