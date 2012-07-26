package org.jreserve.database;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PropertyWriter {
    
    private FileObject file;
    private OutputStream os;
    
    PropertyWriter(FileObject file) {
        this.file = file;
    }
     
    void writeProperties(Properties properties) throws IOException {
        try {
            openFile();
            properties.store(os, null);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeFile();
        }
    }
    
    private void openFile() throws IOException {
        os = file.getOutputStream();
    }
    
    private void closeFile() throws IOException {
        if(os != null)
            os.close();
    }
}
