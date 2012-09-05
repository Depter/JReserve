package org.jreserve.project.system.filesystem;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import org.openide.filesystems.AbstractFileSystem;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PFSAttr  implements AbstractFileSystem.Attr {

    private final ProjectFileSystem pfs;

    PFSAttr(ProjectFileSystem pfs) {
        this.pfs = pfs;
    }
    
    @Override
    public Enumeration<String> attributes(String location) {
        ProjectFile file = pfs.getFileForLocation(location);
        return Collections.enumeration(file.getAttributeNames());
    }

    @Override
    public void deleteAttributes(String location) {
        ProjectFile file = pfs.getFileForLocation(location);
        file.clearAttributes();
    }

    @Override
    public Object readAttribute(String location, String attribute) {
        ProjectFile file = pfs.getFileForLocation(location);
        return file.getAttribute(attribute);
    }

    @Override
    public void writeAttribute(String location, String attribute, Object value) throws IOException {
        ProjectFile file = pfs.getFileForLocation(location);
        file.setAttribute(attribute, value);
    }

    @Override
    public void renameAttributes(String oldLocation, String newLocation) {
    }
}
