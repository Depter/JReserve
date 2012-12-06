package org.jreserve.chart;

import java.awt.Color;

/**
 *
 * @author Peter Decsi
 */
public class ListColorGenerator implements ColorGenerator {

    private Color[] colors;
    private int size;
    private int index = 0;
    
    public ListColorGenerator(Color... colors) {
        size = colors.length;
        this.colors = new Color[size];
        System.arraycopy(colors, 0, this.colors, 0, size);
    }

    @Override
    public Color nextColor() {
        if(index == size)
            index = 0;
        return colors[index++];
    }
}
