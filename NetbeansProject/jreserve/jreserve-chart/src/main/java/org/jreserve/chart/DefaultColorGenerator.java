package org.jreserve.chart;

import java.awt.Color;

/**
 *
 * @author Peter Decsi
 */
public class DefaultColorGenerator implements ColorGenerator {
    
    private static enum Component {
        RED,
        GREEN,
        BLUE
    }
    
    private int value = 255;
    private boolean isDouble = false;
    private Component component = Component.RED;
    private Color next = new Color(255, 0, 0);
    
    @Override
    public Color nextColor() {
        Color result = next;
        
        switch(component) {
            case RED:
                stepFromRed();
                break;
            case GREEN:
                stepFromGreen();
                break;
            default:
                stepFromBlue();
                break;
        }
        
        return result;
    }
    
    private void stepFromRed() {
        component = Component.GREEN;
        next = isDouble? new Color(value, 0, value) : new Color(0, value, 0);
    }
    
    private void stepFromGreen() {
        component = Component.BLUE;
        next = isDouble? new Color(0, value, value) : new Color(0, 0, value);
    }
    
    private void stepFromBlue() {
        component = Component.RED;
        isDouble = !isDouble;
        stepValue();
        next = isDouble? new Color(value, value, 0) : new Color(value, 0, 0);
    }
    
    private void stepValue() {
        if(!isDouble) {
            value = (value==255)? 128 : value/2;
            if(value < 32)
                value = 255;
        }
    }
}
