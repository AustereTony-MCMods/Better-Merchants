package austeretony.better_merchants.client.gui.management.profiles.callback;

import austeretony.better_merchants.client.gui.BackgroundGUIFiller;
import austeretony.better_merchants.client.gui.settings.GUISettings;

public class ProfileCreationCallbackGUIFiller extends BackgroundGUIFiller {

    public ProfileCreationCallbackGUIFiller(int xPosition, int yPosition, int width, int height) {             
        super(xPosition, yPosition, width, height, null);
    }

    @Override
    public void drawDefaultBackground() {
        drawRect(- 1, - 1, this.getWidth() + 1, this.getHeight() + 1, GUISettings.instance().getBaseGUIBackgroundColor());//main background
        drawRect(0, 0, this.getWidth(), 11, GUISettings.instance().getAdditionalGUIBackgroundColor());//title background
        drawRect(0, 12, this.getWidth(), this.getHeight(), GUISettings.instance().getAdditionalGUIBackgroundColor());//rest background
    }
}
