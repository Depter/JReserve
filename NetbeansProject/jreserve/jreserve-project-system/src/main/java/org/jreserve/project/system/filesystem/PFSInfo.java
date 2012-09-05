package org.jreserve.project.system.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import org.openide.filesystems.AbstractFileSystem;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PFSInfo  implements AbstractFileSystem.Info {

    private final ProjectFileSystem pfs;

    PFSInfo(ProjectFileSystem pfs) {
        this.pfs = pfs;
    }
    
    @Override
    public Date lastModified(String location) {
        return new Date();
    }

    @Override
    public boolean folder(String location) {
        ProjectFile file = pfs.getFileForLocation(location);
        return file.isFolder();
    }

    @Override
    public boolean readOnly(String location) {
        return false;
    }

    @Override
    public String mimeType(String location) {
        ProjectFile file = pfs.getFileForLocation(location);
        Object mime = file.getAttribute(ProjectFileSystem.MIME_TYPE);
        return mime==null? null : (String) mime;
    }

    @Override
    public long size(String location) {
        return 0;
    }

    @Override
    public InputStream inputStream(String location) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputStream outputStream(String location) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void lock(String location) throws IOException {
    }

    @Override
    public void unlock(String location) {
    }

    @Override
    public void markUnimportant(String location) {
    }
}
