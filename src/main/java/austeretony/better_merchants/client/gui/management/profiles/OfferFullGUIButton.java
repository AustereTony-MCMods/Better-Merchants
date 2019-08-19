package austeretony.better_merchants.client.gui.management.profiles;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.gui.CurrencyHelper;
import austeretony.better_merchants.client.gui.IndexedGUIButton;
import austeretony.better_merchants.client.gui.ItemRenderHelper;
import austeretony.better_merchants.client.gui.settings.GUISettings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class OfferFullGUIButton extends IndexedGUIButton<Long> {

    private final ItemStack offeredStack, currencyStack;

    private final boolean useCurrencyStack, sellingEnabled, sellingOnly;

    private final String amount, buyCost, sellingCostNotice, sellingOnlyNotice;

    public OfferFullGUIButton(long id, ItemStack offeredStack, int amount, int buyCost, int sellingCost, ItemStack currencyStack) {
        super(id);
        this.offeredStack = offeredStack;
        this.currencyStack = currencyStack;
        this.useCurrencyStack = currencyStack != null;
        this.amount = String.valueOf(amount);
        this.buyCost = String.valueOf(buyCost);
        this.sellingEnabled = sellingCost > 0;
        this.sellingOnly = buyCost == 0;
        this.sellingCostNotice = sellingCost > 0 ? ClientReference.localize("better_merchants.gui.management.sellFor", sellingCost) : ClientReference.localize("better_merchants.gui.management.noSelling");
        this.sellingOnlyNotice = ClientReference.localize("better_merchants.gui.management.sellingOnly");
        this.setDisplayText(this.offeredStack.getDisplayName());
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {          
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);  

            int color;                      
            if (!this.isEnabled())                  
                color = this.getDisabledBackgroundColor();
            else if (this.isHovered() || this.isToggled())                  
                color = this.getHoveredBackgroundColor();
            else                    
                color = this.getEnabledBackgroundColor();                                   
            drawRect(0, 0, this.getWidth(), this.getHeight(), color);

            if (!this.isEnabled())                  
                color = this.getDisabledTextColor();           
            else if (this.isHovered() || this.isToggled())                                          
                color = this.getHoveredTextColor();
            else                    
                color = this.getEnabledTextColor();

            float textScale = GUISettings.instance().getSubTextScale();

            GlStateManager.pushMatrix();           
            GlStateManager.translate(20.0F, 10.0F, 0.0F);            
            GlStateManager.scale(textScale, textScale, 0.0F);   
            this.mc.fontRenderer.drawString(this.amount, 0, 0, color, this.isTextShadowEnabled()); 
            GlStateManager.popMatrix();      

            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getWidth() - 4 - this.textWidth(this.sellingCostNotice, textScale), 10.0F, 0.0F);            
            GlStateManager.scale(textScale, textScale, 0.0F); 
            this.mc.fontRenderer.drawString(this.sellingCostNotice, 0, 0, color, this.isTextShadowEnabled());
            GlStateManager.popMatrix();      

            if (!this.sellingOnly) {
                GlStateManager.pushMatrix();           
                GlStateManager.translate(this.getWidth() - 12 - this.textWidth(this.buyCost, textScale), 1.0F, 0.0F);            
                GlStateManager.scale(textScale, textScale, 0.0F); 
                this.mc.fontRenderer.drawString(this.buyCost, 0, 0, color, this.isTextShadowEnabled());
                GlStateManager.popMatrix(); 

                if (this.useCurrencyStack) {                
                    GlStateManager.pushMatrix();           
                    GlStateManager.translate(this.getWidth() - 10.0F, 0.0F, 0.0F);            
                    GlStateManager.scale(0.5F, 0.5F, 0.5F);    

                    RenderHelper.enableGUIStandardItemLighting();            
                    GlStateManager.enableDepth();
                    ItemRenderHelper.renderItemWithoutEffectIntoGUI(this.currencyStack, 0, 0);                              
                    GlStateManager.disableDepth();
                    RenderHelper.disableStandardItemLighting();

                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.enableBlend(); 
                    this.mc.getTextureManager().bindTexture(CurrencyHelper.instance().getCurrencyTexture());
                    GUIAdvancedElement.drawCustomSizedTexturedRect(this.getWidth() - 9, 1, 0, 0, 5, 5, 5, 5);          
                    GlStateManager.disableBlend();
                } 
            } else {
                GlStateManager.pushMatrix();           
                GlStateManager.translate(this.getWidth() - 4 - this.textWidth(this.sellingOnlyNotice, textScale), 1.0F, 0.0F);            
                GlStateManager.scale(textScale, textScale, 0.0F); 
                this.mc.fontRenderer.drawString(this.sellingOnlyNotice, 0, 0, color, this.isTextShadowEnabled());
                GlStateManager.popMatrix(); 
            }    

            textScale = GUISettings.instance().getTextScale();

            GlStateManager.pushMatrix();           
            GlStateManager.translate(34.0F, (this.getHeight() - this.textHeight(textScale)) / 2.0F + 1.0F, 0.0F);            
            GlStateManager.scale(textScale, textScale, 0.0F);           
            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, color, this.isTextShadowEnabled());
            GlStateManager.popMatrix();     

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); 

            GlStateManager.popMatrix();

            RenderHelper.enableGUIStandardItemLighting();            
            GlStateManager.enableDepth();
            this.itemRender.renderItemAndEffectIntoGUI(this.offeredStack, this.getX() + 4, this.getY());   
            GlStateManager.disableDepth();            
            RenderHelper.disableStandardItemLighting();
        }     
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {
        if (mouseX >= this.getX() + 4 && mouseY >= this.getY() && mouseX < this.getX() + 20 && mouseY < this.getY() + this.getHeight())
            this.screen.drawToolTip(this.offeredStack, mouseX + 6, mouseY);
    }
}
