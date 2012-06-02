package org.decsi.jreserve.gui;

import javax.swing.JFrame;
import org.swixml.SwingEngine;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MainFrame implements XmlGui {
    
    private final static String DESCRIPTOR = "main_frame.xml";
    
    private SwingEngine swix = new SwingEngine(this);
    
    public JFrame mainFrame;
    
    public MainFrame() throws Exception {
    }

    @Override
    public String getXmlDescriptor() {
        return DESCRIPTOR;
    }
    
    public static void open() throws Exception {
        MainFrame main = new MainFrame();
        GuiLoader.loadGuiXml(main);
        main.mainFrame.setVisible(true);
    }
}
