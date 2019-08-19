package austeretony.better_merchants.client.gui.merchant;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.main.MerchantProfile;
import net.minecraft.util.ResourceLocation;

public class MerchantMenuGUIScreen extends AbstractGUIScreen {

    public static final ResourceLocation MERCHANT_MENU_BACKGROUND = new ResourceLocation(BetterMerchantsMain.MODID, "textures/gui/merchant/merchant_menu.png");

    public final MerchantProfile merchantProfile;

    protected BuyGUISection buySection;

    protected SellingGUISection sellingSection;

    public MerchantMenuGUIScreen(MerchantProfile profile) {
        this.merchantProfile = profile;
    }

    @Override
    protected GUIWorkspace initWorkspace() {
        return new GUIWorkspace(this, 173, 189).setAlignment(EnumGUIAlignment.RIGHT, - 10, 0);
    }

    @Override
    protected void initSections() {
        this.getWorkspace().initSection(this.buySection = new BuyGUISection(this));    
        this.getWorkspace().initSection(this.sellingSection = new SellingGUISection(this));        
    }

    @Override
    protected AbstractGUISection getDefaultSection() {
        return this.buySection;
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}

    @Override
    protected boolean doesGUIPauseGame() {
        return false;
    }

    public BuyGUISection getBuySection() {
        return this.buySection;
    }

    public SellingGUISection getSellingSection() {
        return this.sellingSection;
    }
}
