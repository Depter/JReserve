package org.jreserve.logging;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        LoggingSetting.initialize();
//        initializeLoggingLevel(prefs);
//        initializeGuiAppender(prefs);
    }
    
//    private void initializeLoggingLevel(Preferences prefs) {
//        LoggingSetting.setLevel(LoggingPanel.getLevel(prefs));
//    }
//    
//    private void initializeGuiAppender(Preferences prefs) {
//        if(!prefs.getBoolean(LoggingPanel.SHOW_LOG_KEY, false))
//            return;
//        try {
//            openLogview();
//        } catch (Exception ex) {
//            Exceptions.printStackTrace(ex);
//        }
//    }
//
//    private void openLogview() throws Exception {
//        SwingUtilities.invokeAndWait(new Runnable() {
//            @Override
//            public void run() {
//                LogviewTopComponent.openView();
//                LoggingSetting.appendGuiAppender();
//            }
//        });
//    }
}
