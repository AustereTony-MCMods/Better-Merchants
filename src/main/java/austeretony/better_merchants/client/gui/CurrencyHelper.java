package austeretony.better_merchants.client.gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;

import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.common.CommonReference;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.util.JsonUtils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public final class CurrencyHelper {

    private static CurrencyHelper instance;

    private ResourceLocation currencyTexture = new ResourceLocation(BetterMerchantsMain.MODID, "textures/gui/currency_icon.png");

    private String currencyName = "better_merchants.gui.currency";

    private CurrencyHelper() {
        this.load();
    }

    public static void create() {
        if (instance == null)
            instance = new CurrencyHelper();
    }

    public static CurrencyHelper instance() {
        return instance;
    }

    public ResourceLocation getCurrencyTexture() {
        return this.currencyTexture;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public void load() {
        this.loadCurrencyTexture();
        this.loadCurrencyData();
    }

    private void loadCurrencyTexture() {
        String 
        internalPathStr = "assets/better_merchants/textures/gui/currency.png",
        externalPathStr = CommonReference.getGameFolder() + "/config/better merchants/client/currency/currency.png";
        Path externalPath =  Paths.get(externalPathStr);
        if (Files.exists(externalPath)) {
            try {
                this.currencyTexture = ClientReference.getMinecraft().getTextureManager().getDynamicTextureLocation("currency", new DynamicTexture(ImageIO.read(new File(externalPathStr))));
                BetterMerchantsMain.LOGGER.error("Loaded custom currency texture. Path: {}.", externalPathStr);
            } catch (IOException exception) {
                BetterMerchantsMain.LOGGER.error("Failed to load currency texture. Path: {}.", externalPathStr);
                exception.printStackTrace();
            }        
        }
    }

    private void loadCurrencyData() {
        String 
        internalPathStr = "assets/better_merchants/currency_data.json",
        externalPathStr = CommonReference.getGameFolder() + "/config/better merchants/client/currency/currency_data.json";
        Path externalPath =  Paths.get(externalPathStr);
        if (Files.exists(externalPath)) {
            try {
                JsonObject externalFile = JsonUtils.getExternalJsonData(externalPathStr).getAsJsonObject();
                this.currencyName = externalFile.get("name").getAsString();
                BetterMerchantsMain.LOGGER.error("Loaded custom currency name <{}>. Path: {}.", this.currencyName, externalPathStr);
            } catch (IOException exception) {
                BetterMerchantsMain.LOGGER.error("Failed to load currency data. Path: {}.", externalPath);
                exception.printStackTrace();
            }
        }
    }
}
