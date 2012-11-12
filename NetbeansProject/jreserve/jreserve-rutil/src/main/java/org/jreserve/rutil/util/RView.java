package org.jreserve.rutil.util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.text.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RView extends PlainView {

    public RView(Element element) {
        super(element);
    }
    
    @Override
    protected int drawUnselectedText(Graphics graphics, int x, int y, int p0, int p1) throws BadLocationException {
        Document doc = getDocument();
        String text = doc.getText(p0, p1 - p0);
        
        Segment segment = getLineBuffer();
        Map<Integer, Integer> startMap = new TreeMap<Integer, Integer>();
        Map<Integer, Color> colorMap = new TreeMap<Integer, Color>();
        
        
        return super.drawUnselectedText(graphics, x, y, p0, p1);
    }
}
