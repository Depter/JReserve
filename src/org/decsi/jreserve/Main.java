package org.decsi.jreserve;

import org.decsi.jreserve.gui.MainFrame;
import org.decsi.jreserve.persistance.Persistance;
import org.decsi.jreserve.settings.Settings;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            startProgram();
        } catch (Exception ex) {
            //TODO exit
        }
    }
    
    private static void startProgram() throws Exception {
        Settings.load();
        Persistance.load(Settings.getPersistanceClass());
        Persistance.getManager().getLoBs();
        loadGUI();
    }
    
    private static void loadGUI() throws Exception {
        MainFrame.open();
    }
}
