package org.jreserve.database.derby.create;

import java.io.File;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DerbyDirectoryValidator {
    
    private final File home;
    
    DerbyDirectoryValidator(File home) {
        this.home = home;
    }
    
    boolean isDerbyDatabase() {
        if(home.isDirectory())
            return isDerbyContent();
        return false;
    }
    
    private boolean isDerbyContent() {
        return directoryExists("log") &&
               directoryExists("seg0") &&
               fileExists("service.properties");
    }
    
    private boolean directoryExists(String name) {
        File dir = new File(home, name);
        return dir.exists() && 
               dir.isDirectory();
    }
    
    private boolean fileExists(String name) {
        File file = new File(home, name);
        return file.exists() &&
               file.isFile();
    }
}
