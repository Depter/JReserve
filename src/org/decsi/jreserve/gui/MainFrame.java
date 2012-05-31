package org.decsi.jreserve.gui;

import java.awt.Image;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.swixml.SwingEngine;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MainFrame {
    
    private final static String DESCRIPTOR = "res/guixml/main_frame.xml";
    private final static String ICON = "res/img/app_Icon16.png";
    
    private SwingEngine swix = new SwingEngine(this);
    
    public JFrame mainFrame;
    
    public MainFrame() throws Exception {
        URL url = MainFrame.class.getClassLoader().getResource(DESCRIPTOR);
        swix.render(url);
        
        url = MainFrame.class.getClassLoader().getResource(ICON);
        System.out.println(url.getPath());
        ImageIcon image = new ImageIcon(url);
        Image icon = image.getImage();
        //mainFrame.setIconImage(icon);
        mainFrame.setVisible(true);
    }
    
    public static void main(String[] args) {
        try {
            new MainFrame();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}
