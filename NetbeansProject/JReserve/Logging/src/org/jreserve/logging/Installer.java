/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.logging;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

class Installer extends ModuleInstall {
    private final static String LAYOUT = "%d %-5p %m%n";

    @Override
    public void restored() {
        Logger root = LogManager.getRootLogger();
        try {
            root.addAppender(new FileAppender(new PatternLayout(LAYOUT), getLogFile(), false));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private static String getLogFile() {
        File home = new File(System.getProperty("netbeans.user"));
        home = new File(home, "Logging");
        if(!home.exists())
            home.mkdir();
        return new File(home, "jreserve.log").getPath();
    }
    
}
