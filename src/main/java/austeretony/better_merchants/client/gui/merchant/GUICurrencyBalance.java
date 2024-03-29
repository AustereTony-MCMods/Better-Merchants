package austeretony.better_merchants.client.gui.merchant;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.gui.CurrencyHelper;
import austeretony.better_merchants.client.gui.ItemRenderHelper;
import austeretony.better_merchants.client.gui.settings.GUISettings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class GUICurrencyBalance extends GUISimpleElement<GUICurrencyBalance> {

    private ItemStack itemStack;

    public GUICurrencyBalance(int x, int y) {
        this.setPosition(x, y);
        this.enableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.itemStack == null) {
                GlStateManager.enableBlend(); 
                this.mc.getTextureManager().bindTexture(CurrencyHelper.instance().getCurrencyTexture());
                GUIAdvancedElement.drawCustomSizedTexturedRect(0, 0, 0, 0, 5, 5, 5, 5);          
                GlStateManager.disableBlend();
                GlStateManager.pushMatrix();           
                GlStateManager.translate(- 2.0F - this.textWidth(this.getDisplayText(), GUISettings.instance().getSubTextScale()), 0.0F, 0.0F);            
                GlStateManager.scale(GUISettings.instance().getSubTextScale(), GUISettings.instance().getSubTextScale(), 0.0F);                                      
                this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, this.getEnabledTextColor(), false);
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
                GlStateManager.translate(- 3.0F - this.textWidth(this.getDisplayText(), GUISettings.instance().getSubTextScale()), 0.0F, 0.0F);            
                GlStateManager.scale(GUISettings.instance().getSubTextScale(), GUISettings.instance().getSubTextScale(), 0.0F);                                      
                this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, this.getEnabledTextColor(), false);
                GlStateManager.popMatrix();
            } 
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {
        if (this.itemStack != null && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + 8 && mouseY < this.getY() + 8)
            this.screen.drawToolTip(this.itemStack, 
                    mouseX - this.textWidth(this.itemStack.getDisplayName(), 1.0F) - (ClientReference.getGameSettings().advancedItemTooltips ? 75 : 25), mouseY);
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setBalance(int balance) {
        this.setDisplayText(String.valueOf(balance));
    }
}
