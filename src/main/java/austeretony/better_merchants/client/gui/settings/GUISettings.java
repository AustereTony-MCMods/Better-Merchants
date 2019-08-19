package austeretony.better_merchants.client.gui.settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonObject;

import austeretony.better_merchants.common.CommonReference;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.util.JsonUtils;

public final class GUISettings {

    private static GUISettings instance;

    private GUISettingsProfile currentProfile;

    private GUISettings() {
        this.loadSettings();
    }

    public static void create() {
        if (instance == null)
            instance = new GUISettings();
    }

    public static GUISettings instance() {
        return instance;
    }

    public void loadSettings() {
        String 
        internalPathStr = "assets/better_merchants/gui_settings.json",
        externalPathStr = CommonReference.getGameFolder() + "/config/better merchants/client/gui_setting.json";
        Path externalPath =  Paths.get(externalPathStr);
        if (Files.exists(externalPath)) {
            try {      
                this.loadData(JsonUtils.getExternalJsonData(externalPathStr).getAsJsonObject());
            } catch (IOException | NullPointerException exception) {  
                BetterMerchantsMain.LOGGER.error("External GUI settings file damaged or outdated! Creating default config...");
                try {
                    this.createExternalCopyAndLoad(JsonUtils.getInternalJsonData(internalPathStr).getAsJsonObject(), externalPathStr);
                } catch (IOException e) {
                    BetterMerchantsMain.LOGGER.error("Internal GUI settings file damaged!");
                    e.printStackTrace();
                }  
            }       
        } else {                
            try {               
                Files.createDirectories(externalPath.getParent());                                
                this.createExternalCopyAndLoad(JsonUtils.getInternalJsonData(internalPathStr).getAsJsonObject(), externalPathStr);  
            } catch (IOException exception) {   
                BetterMerchantsMain.LOGGER.error("Internal GUI settings file damaged!");
                exception.printStackTrace();
            }     
        }      
    }

    private void createExternalCopyAndLoad(JsonObject internalFile, String externalFile) {       
        try {           
            JsonUtils.createExternalJsonFile(externalFile, internalFile);            
        } catch (IOException exception) {       
            BetterMerchantsMain.LOGGER.error("External GUI settings file creation failed.");
            exception.printStackTrace();
        }
        this.loadData(internalFile);
    }

    private void loadData(JsonObject file) {
        this.setCurrentProfile(GUISettingsProfile.deserialize(file));
        BetterMerchantsMain.LOGGER.info("GUI settings loaded.");
    }

    public GUISettingsProfile getCurrentProfile() {
        return this.currentProfile;
    }

    public void setCurrentProfile(GUISettingsProfile profile) {
        this.currentProfile = profile;
    }

    public int getTextureOffsetX() {
        return this.currentProfile.textureOffsetX;
    }

    public int getTextureOffsetY() {
        return this.currentProfile.textureOffsetY;
    }

    public int getBaseGUIBackgroundColor() {
        return this.currentProfile.baseGUIBackgroundColor;
    }

    public int getAdditionalGUIBackgroundColor() {
        return this.currentProfile.additionalGUIBackgroundColor;
    }

    public int getPanelGUIBackgroundColor() {
        return this.currentProfile.panelGUIBackgroundColor;
    }

    public int getEnabledButtonColor() {
        return this.currentProfile.enabledButtonColor;
    }

    public int getDisabledButtonColor() {
        return this.currentProfile.disabledButtonColor;
    }

    public int getHoveredButtonColor() {
        return this.currentProfile.hoveredButtonColor;
    }

    public int getEnabledElementColor() {
        return this.currentProfile.enabledElementColor;
    }

    public int getDisabledElementColor() {
        return this.currentProfile.disabledElementColor;
    }

    public int getHoveredElementColor() {
        return this.currentProfile.hoveredElementColor;
    }

    public int getEnabledContextActionColor() {
        return this.currentProfile.enabledContextActionColor;
    }

    public int getDisabledContextActionColor() {
        return this.currentProfile.disabledContextActionColor;
    }

    public int getHoveredContextActionColor() {
        return this.currentProfile.hoveredContextActionColor;
    }

    public int getEnabledSliderColor() {
        return this.currentProfile.enabledSliderColor;
    }

    public int getDisabledSliderColor() {
        return this.currentProfile.disabledSliderColor;
    }

    public int getHoveredSliderColor() {
        return this.currentProfile.hoveredSliderColor;
    }

    public int getEnabledTextColor() {
        return this.currentProfile.enabledTextColor;
    }

    public int getDisabledTextColor() {
        return this.currentProfile.disabledTextColor;
    }

    public int getHoveredTextColor() {
        return this.currentProfile.hoveredTextColor;
    }

    public int getEnabledTextColorDark() {
        return this.currentProfile.enabledTextColorDark;
    }

    public int getDisabledTextColorDark() {
        return this.currentProfile.disabledTextColorDark;
    }

    public int getHoveredTextColorDark() {
        return this.currentProfile.hoveredTextColorDark;
    }

    public int getEnabledTextFieldColor() {
        return this.currentProfile.enabledTextFieldColor;
    }

    public int getDisabledTextFieldColor() {
        return this.currentProfile.disabledTextFieldColor;
    }

    public int getHoveredTextFieldColor() {
        return this.currentProfile.hoveredTextFieldColor;
    }

    public int getBaseOverlayTextColor() {
        return this.currentProfile.baseOverlayTextColor;
    }

    public int getAdditionalOverlayTextColor() {
        return this.currentProfile.additionalOverlayTextColor;
    }

    public int getTooltipBackgroundColor() {
        return this.currentProfile.tooltipBackgroundColor;
    }

    public int getTooltipTextColor() {
        return this.currentProfile.tooltipTextColor;
    }

    public float getTitleScale() {
        return this.currentProfile.titleScale;
    }

    public float getButtonTextScale() {
        return this.currentProfile.buttonTextScale;
    }

    public float getTooltipScale() {
        return this.currentProfile.tooltipScale;
    }

    public float getTextScale() {
        return this.currentProfile.textScale;
    }

    public float getSubTextScale() {
        return this.currentProfile.subTextScale;
    }

    public float getPanelTextScale() {
        return this.currentProfile.panelTextScale;
    }

    public float getContextMenuScale() {
        return this.currentProfile.contextMenuScale;
    }

    public float getOverlayScale() {
        return this.currentProfile.overlayScale;
    }

    public int getContextMenuWidth() {
        return this.currentProfile.contextMenuWidth;
    }
}
