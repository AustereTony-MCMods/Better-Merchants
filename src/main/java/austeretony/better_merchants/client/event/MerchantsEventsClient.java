package austeretony.better_merchants.client.event;

import org.lwjgl.input.Mouse;

import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.client.gui.settings.GUISettings;
import austeretony.better_merchants.common.main.EntityEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;

public class MerchantsEventsClient {

    private Entity pointed;

    @SubscribeEvent
    public void onMouseInput(MouseInputEvent event) { 
        if (Mouse.getEventButton() == 1 
                && Mouse.getEventButtonState())
            if (this.isValid())
                this.execute();
    }

    public boolean isValid() {
        return this.pointed != null 
                && this.pointed instanceof EntityLiving 
                && MerchantsManagerClient.instance().getEntitiesManager().entryExist(ClientReference.getPersistentUUID(this.pointed));
    }

    public void execute() {
        MerchantsManagerClient.instance().openMerchantMenuSynced(ClientReference.getEntityId(this.pointed));
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.TEXT) {
            if (this.shouldDraw() && ClientReference.getMinecraft().inGameHasFocus)
                this.draw(event.getPartialTicks());
        }
    }

    public boolean shouldDraw() {
        this.pointed = ClientReference.getPointedEntity();
        return this.pointed != null 
                && this.pointed instanceof EntityLiving 
                && ClientReference.isEntitiesNear(this.pointed, ClientReference.getClientPlayer(), 3.0D)
                && MerchantsManagerClient.instance().getEntitiesManager().entryExist(ClientReference.getPersistentUUID(this.pointed));
    }

    public void draw(float partialTicks) {
        Minecraft mc = ClientReference.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);  
        int 
        x = scaledResolution.getScaledWidth() / 2 + 10,
        y = scaledResolution.getScaledHeight() / 2,
        keyNameWidth;

        GlStateManager.pushMatrix();    
        GlStateManager.translate(x, y, 0.0F);          
        GlStateManager.scale(GUISettings.instance().getOverlayScale(), GUISettings.instance().getOverlayScale(), 0.0F);         

        EntityEntry entry = MerchantsManagerClient.instance().getEntitiesManager().getEntityEntry(ClientReference.getPersistentUUID(this.pointed));
        if (!entry.profession.isEmpty())
            mc.fontRenderer.drawString(entry.name + ", " + entry.profession, 0, 0, GUISettings.instance().getAdditionalOverlayTextColor(), true);
        else
            mc.fontRenderer.drawString(entry.name, 0, 0, GUISettings.instance().getAdditionalOverlayTextColor(), true);

        mc.fontRenderer.drawString(ClientReference.localize("better_merchants.gui.management.merchantProfession"), 0, 12, GUISettings.instance().getBaseOverlayTextColor(), true);

        mc.fontRenderer.drawString(ClientReference.localize("better_merchants.overlay.interact"), 0, 25, GUISettings.instance().getAdditionalOverlayTextColor(), true);

        GlStateManager.popMatrix();
    }
}