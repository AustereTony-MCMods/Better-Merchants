package austeretony.alternateui.screen.list;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.GUISoundEffect;
import net.minecraft.util.SoundEvent;

/**
 * Выпадающий список для ГПИ.
 * 
 * @author AustereTony
 */
public class GUIDropDownList extends GUISimpleElement<GUIDropDownList> {

    public final List<GUIDropDownElement> 
    visibleElements = new ArrayList<GUIDropDownElement>(5),
    elementsBuffer = new ArrayList<GUIDropDownElement>(5);

    private int elementsOffset;

    private GUIDropDownElement choosenElement, hoveredElement;

    private boolean resetScroller, hasChoosenElement;

    private int actionBoxWidth, actionBoxHeight;

    private GUISoundEffect openSound, closeSound;

    public GUIDropDownList(int xPosition, int yPosition, int buttonWidth, int buttonHeight) {   	
        this.setPosition(xPosition, yPosition);
        this.actionBoxWidth = buttonWidth;
        this.actionBoxHeight = buttonHeight;
        this.enableFull();
    }

    /**
     * Метод для добавления элемента в список.
     * 
     * @param dropDownElement добавляемый элемент
     * 
     * @return вызывающий объект
     */
    public GUIDropDownList addElement(GUIDropDownElement dropDownElement) {
        if (this.visibleElements.size() == 0)
            this.setSize((int) ((float) this.actionBoxWidth * this.getScale()), (int) ((float) this.actionBoxHeight * this.getScale()));  
        int size;		
        dropDownElement.initScreen(this.getScreen()); 
        dropDownElement.setTextScale(this.getTextScale());
        dropDownElement.setTextAlignment(this.getTextAlignment(), this.getTextOffset());
        this.bind(dropDownElement);
        if (!this.visibleElements.contains(dropDownElement)) {   		
            size = this.visibleElements.size();    		
            if (!this.hasScroller()) {   		
                dropDownElement.setPosition(this.getX(), this.getY() + (int) (this.getHeight() * this.getScale() * (size + 1)));  		
                dropDownElement.setSize(this.actionBoxWidth, this.actionBoxHeight);   		
                dropDownElement.setScale(this.getScale());   	
                dropDownElement.setTextAlignment(this.getTextAlignment(), this.getTextOffset());    		    		
                this.visibleElements.add(dropDownElement);
            }
        }    	
        if (!this.elementsBuffer.contains(dropDownElement)) {    		
            size = this.elementsBuffer.size();    		
            dropDownElement.setPosition(this.getX(), this.getY() + (int) (this.getHeight() * this.getScale() * (size + 1)));  		
            dropDownElement.setSize(this.getWidth(), this.getHeight());   		
            dropDownElement.setScale(this.getScale());
            dropDownElement.setTextAlignment(this.getTextAlignment(), this.getTextOffset());    		
            this.elementsBuffer.add(dropDownElement);
            dropDownElement.init();
        }    	    	
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY) {   
        super.draw(mouseX, mouseY);
        if (this.isVisible())        	
            if (this.isDragged())        	
                for (GUIDropDownElement element : this.visibleElements)         	
                    element.draw(mouseX, mouseY);          	
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {   	
        if (this.isVisible() && this.isDragged())              	
            for (GUIDropDownElement element : this.visibleElements)              	
                element.drawTooltip(mouseX, mouseY);          	
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {    	
        if (this.isEnabled()) {
            this.setHovered(mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + (this.isDragged() ? this.getHeight() * this.visibleElements.size() : this.getHeight()));   
            if (this.isDragged()) {    	
                for (GUIDropDownElement element : this.visibleElements) {
                    element.mouseOver(mouseX, mouseY);   			
                    if (element.isHovered())   				
                        this.hoveredElement = element;
                }
            }
        } 
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {    	
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);    	      	    	
        if (flag && mouseButton == 0) {    		 
            for (GUIDropDownElement element : this.visibleElements) {    			
                if (element.mouseClicked(mouseX, mouseY, mouseButton)) {  				    				
                    this.choosenElement = element;    				
                    this.setDisplayText(element.getDisplayText());   				
                    this.hasChoosenElement = true;    				
                    this.setDragged(false);   		    	
                    element.setHovered(false);    		    	
                    if (this.shouldResetScrollerOnClosing()) 		    		
                        this.reset();		    	
                    this.screen.handleElementClick(this.screen.getWorkspace().getCurrentSection(), element);   		    	
                    this.screen.getWorkspace().getCurrentSection().handleElementClick(this.screen.getWorkspace().getCurrentSection(), element, mouseButton);    							
                    if (this.screen.getWorkspace().getCurrentSection().hasCurrentCallback())				
                        this.screen.getWorkspace().getCurrentSection().getCurrentCallback().handleElementClick(this.screen.getWorkspace().getCurrentSection(), element, mouseButton);
                    if (this.closeSound != null)  
                        this.mc.player.playSound(this.closeSound.sound, this.closeSound.volume, this.closeSound.pitch);
                    return true;
                }
            }
        }    	
        if (flag && mouseButton == 0 && !this.isDragged())
            if (this.openSound != null)  
                this.mc.player.playSound(this.openSound.sound, this.openSound.volume, this.openSound.pitch);
        this.setDragged(flag && mouseButton == 0);    	
        if (this.shouldResetScrollerOnClosing()) 		
            this.reset();
        return false;
    }

    @Override
    public void handleScroller(boolean isScrolling) {    	
        if (this.hasScroller()) {   			
            if (this.isHovered() || this.getScroller().shouldIgnoreBorders())   			    	    			    				    					    	    			
                if (this.getScroller().handleScroller())    				    	    				  	    		
                    this.screen.scrollDropDownList(this);
        }
    }

    private void reset() {    	    	
        int i = 0, size;    	
        GUIDropDownElement dropDownElement;    	    		
        this.visibleElements.clear();    	
        this.getScroller().resetPosition();   	    		
        for (i = 0; i < this.getVisibleElementsAmount(); i++) {    		
            if (i < this.elementsBuffer.size()) {			
                dropDownElement = this.elementsBuffer.get(i);  			            	            	
                size = this.visibleElements.size();				    				
                dropDownElement.setPosition(this.getX(), this.getY() + (size + 1) * this.getHeight() - (size / this.getVisibleElementsAmount()) * (this.getMaxElementsAmount() * this.getHeight()));				
                this.visibleElements.add(dropDownElement);
            }
        }
    }

    public boolean hasChoosenElement() {   	
        return this.hasChoosenElement;
    }

    /**
     * Возвращает выбранный элемент списка или null, если его нет.
     * 
     * @return выбранный элемент
     */
    public GUIDropDownElement getChoosenElement() {    	
        return this.choosenElement;
    }

    /**
     * Возвращает элемент списка над которым находится курсор или null, если его нет.
     * 
     * @return элемент списка над которым находится курсор
     */
    public GUIDropDownElement getHoveredElement() {    	
        return this.hoveredElement;
    }

    public boolean shouldResetScrollerOnClosing() {    	
        return this.resetScroller;
    }

    /**
     * При закрытии списка скроллер будет обнулять позицию.
     * 
     * @return вызывающий объект
     */
    public GUIDropDownList resetScrollerOnClosing() {    	
        this.resetScroller = true;    	
        return this;
    }   

    public GUIDropDownList setOpenSound(GUISoundEffect sound) {
        this.openSound = sound;
        return this;
    }

    public GUIDropDownList setOpenSound(SoundEvent sound) {
        this.openSound = new GUISoundEffect(sound, 0.5F, 1.0F);
        return this;
    }

    public GUIDropDownList setCloseSound(GUISoundEffect sound) {
        this.closeSound = sound;
        return this;
    }

    public GUIDropDownList setCloseSound(SoundEvent sound) {
        this.closeSound = new GUISoundEffect(sound, 0.5F, 1.0F);
        return this;
    }
}
