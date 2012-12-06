package org.jreserve.database;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 */
public class Util {

    private final static FileSystem FS = FileUtil.createMemoryFileSystem();
    
    public static FileObject createTextFile(String name) throws IOException {
        FileObject file = FS.getRoot().createData(name, "txt");
        return file;
    }
    
    public static FileObject getTextFile(String name) throws IOException {
        return FS.getRoot().getFileObject(name, "txt");
    }
    
    public static void deleteTextFile(String name) throws IOException {
        FS.getRoot().getFileObject(name, "txt").delete();
    }
}
