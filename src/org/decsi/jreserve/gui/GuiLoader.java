package org.decsi.jreserve.gui;

import org.swixml.SwingEngine;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GuiLoader {
    
    private final static String DESCRIPTOR_HOME = "res/guixml/";
        
    private GuiLoader() {}
    
    public static void loadGuiXml(XmlGui target) {
        try {
            String descriptor = DESCRIPTOR_HOME + target.getXmlDescriptor();
            SwingEngine swix = new SwingEngine(target);
            swix.render(descriptor);
        } catch (Exception ex) {
            //TODO log exception
        }
    }
}
