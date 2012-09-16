/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.logging.settings;

import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import org.jreserve.logging.view.LogviewTopComponent;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        LoggingSetting.initialize();
        Preferences prefs = NbPreferences.forModule(LoggingPanel.class);
        initializeLoggingLevel(prefs);
        initializeGuiAppender(prefs);
    }
    
    private void initializeLoggingLevel(Preferences prefs) {
        LoggingSetting.setLevel(LoggingPanel.getLevel(prefs));
    }
    
    private void initializeGuiAppender(Preferences prefs) {
        if(!prefs.getBoolean(LoggingPanel.SHOW_LOG_KEY, false))
            return;
        try {
            openLogview();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void openLogview() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                LogviewTopComponent.openView();
                LoggingSetting.appendGuiAppender();
            }
        });
    }
}
