package austeretony.better_merchants.client.gui.management;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;

public class ManagementMenuGUIScreen extends AbstractGUIScreen {

    public static boolean screenInitialized, dataReceived;

    private ProfilesManagementGUISection profilesSection;

    private boolean initialized;

    @Override
    protected GUIWorkspace initWorkspace() {
        return new GUIWorkspace(this, 289, 149);
    }

    @Override
    protected void initSections() {
        this.getWorkspace().initSection(this.profilesSection = new ProfilesManagementGUISection(this));   
    }

    @Override
    protected AbstractGUISection getDefaultSection() {
        return this.profilesSection;
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}

    @Override
    protected boolean doesGUIPauseGame() {
        return false;
    }

    public void loadData() {
        this.profilesSection.sortProfiles(0);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!this.initialized && screenInitialized && dataReceived) {
            this.initialized = true;
            this.loadData();
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        screenInitialized = false;
        dataReceived = false;
    }

    public ProfilesManagementGUISection getProfilesSection() {
        return this.profilesSection;
    }
}
