package org.jreserve.resources;

import java.awt.Component;
import java.awt.Cursor;
import javax.swing.JFrame;
import org.openide.util.Mutex;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WindowUtil {

    public static void showWaitCursor() {
        Mutex.EVENT.writeAccess (new Runnable() { 
            @Override
            public void run () { 
                try { 
                    Component glassPane = getMainGlassPane();
                    glassPane.setVisible(true); 
                    glassPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); 
                } catch (Exception e) { 
                } 
            } 
        }); 
    }
    
    private static Component getMainGlassPane() {
        JFrame mainFraome = (JFrame) WindowManager.getDefault().getMainWindow();
        return mainFraome.getGlassPane();
    }

    public static void showNormalCursor() {
        Mutex.EVENT.writeAccess (new Runnable() { 
            @Override
            public void run () { 
                try { 
                    Component glassPane = getMainGlassPane();
                    glassPane.setVisible(false); 
                    glassPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 
                } catch (Exception e) { 
                } 
            } 
        }); 
    }
    
    private WindowUtil() {}
}
