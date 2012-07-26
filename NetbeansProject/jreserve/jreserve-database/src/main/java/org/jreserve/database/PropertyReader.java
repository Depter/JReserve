package org.jreserve.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PropertyReader {
    
    private FileObject file;
    private FileLock lock;
    private InputStream is;
    
    PropertyReader(FileObject file) {
        this.file = file;
    }
     
    Properties readProperties() throws IOException {
        try {
            openFile();
            return readFromStream();
        } finally {
            closeFile();
        }
    }
    
    private void openFile() throws IOException {
        lock = file.lock();
        is = file.getInputStream();
    }
    
    private Properties readFromStream() throws IOException {
        Properties properties = new Properties();
        properties.load(is);
        return properties;
    }
    
    private void closeFile() throws IOException {
        try {
            if(is != null)
                is.close();
        } finally {
            lock.releaseLock();
        }
    }
}
