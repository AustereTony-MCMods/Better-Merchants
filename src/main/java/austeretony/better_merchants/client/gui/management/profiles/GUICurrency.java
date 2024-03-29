package austeretony.better_merchants.client.gui.management.profiles;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.gui.CurrencyHelper;
import austeretony.better_merchants.client.gui.ItemRenderHelper;
import austeretony.better_merchants.client.gui.settings.GUISettings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class GUICurrency extends GUISimpleElement<GUICurrency> {

    private ItemStack itemStack;

    private boolean useCurrency;

    public GUICurrency(int x, int y) {
        this.setPosition(x, y);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isEnabled()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.useCurrency) {
                GlStateManager.enableBlend(); 
                this.mc.getTextureManager().bindTexture(CurrencyHelper.instance().getCurrencyTexture());
                GUIAdvancedElement.drawCustomSizedTexturedRect(0, 0, 0, 0, 5, 5, 5, 5);          
                GlStateManager.disableBlend();
                GlStateManager.pushMatrix();           
                GlStateManager.translate(8.0F, 0.0F, 0.0F);            
                GlStateManager.scale(GUISettings.instance().getSubTextScale(), GUISettings.instance().getSubTextScale(), 0.0F);                                      
                this.mc.fontRenderer.drawString(ClientReference.localize(CurrencyHelper.instance().getCurrencyName()), 0, 0, this.getEnabledTextColor(), false);
                GlStateManager.popMatrix();
            } else {
                GlStateManager.pushMatrix();           
                GlStateManager.translate(0.0F, - 2.0F, 0.0F);            
                GlStateManager.scale(0.5F, 0.5F, 0.5F);     

                RenderHelper.enableGUIStandardItemLighting();            
                GlStateManager.enableDepth();
                ItemRenderHelper.renderItemWithoutEffectIntoGUI(this.itemStack, 0, 0);                              
                GlStateManager.disableDepth();
                RenderHelper.disableStandardItemLighting();

                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();           
                GlStateManager.translate(10.0F, 0.0F, 0.0F);            
                GlStateManager.scale(GUISettings.instance().getSubTextScale(), GUISettings.instance().getSubTextScale(), 0.0F);                                      
                this.mc.fontRenderer.drawString(this.itemStack.getDisplayName(), 0, 0, this.getEnabledTextColor(), false);
                GlStateManager.popMatrix();
            } 
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {
        if (!this.useCurrency && this.itemStack != null && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + 8 && mouseY < this.getY() + 8)
            this.screen.drawToolTip(this.itemStack, mouseX + 6, mouseY);
    }

    public void setUseCurrency() {
        this.useCurrency = true;
    }

    public void setUseItem(ItemStack itemStack) {
        this.useCurrency = false;
        this.itemStack = itemStack;
    }

    public boolean getUseCurrency() {
        return this.useCurrency;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}
