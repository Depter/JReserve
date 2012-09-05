package org.jreserve.project.system.filesystem;

import java.io.IOException;
import org.openide.filesystems.AbstractFileSystem;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PFSChange implements AbstractFileSystem.Change {

    private final ProjectFileSystem pfs;

    PFSChange(ProjectFileSystem pfs) {
        this.pfs = pfs;
    }
    
    @Override
    public void createFolder(String location) throws IOException {
        ProjectFile parent = getParent(location);
        String name = ProjectFileSystem.getName(location);
        parent.createChild(name);
    }
        
    private ProjectFile getParent(String location) {
        String[] path = ProjectFileSystem.getPath(location);
        String[] parentPath = new String[path.length - 1];
        System.arraycopy(path, 0, parentPath, 0, parentPath.length);
        return pfs.getFileForPath(parentPath);
    }
        
    @Override
    public void createData(String location) throws IOException {
        createFolder(location);
    }

    @Override
    public void rename(String oldLocation, String newLocation) throws IOException {
        ProjectFile file = pfs.getFileForLocation(oldLocation);
        ProjectFile parent = file.getParent();
        ProjectFile newParent = getParent(newLocation);
        moveFile(file, parent, newParent);
        renameFile(file, newLocation);
    }
        
    private void moveFile(ProjectFile file, ProjectFile parent, ProjectFile newParent) {
        String oldLocation = parent==null? "" : parent.getPath();
        String newLocation = parent==null? "" : newParent.getPath();
        if(oldLocation.equalsIgnoreCase(newLocation))
                return;
        file.delete();
        newParent.addChild(file);
    }
        
    private void renameFile(ProjectFile file, String newLocation) {
        String name = ProjectFileSystem.getName(newLocation);
        if(!file.getName().equalsIgnoreCase(name))
                file.setName(name);
    }

    @Override
    public void delete(String location) throws IOException {
        ProjectFile file = pfs.getFileForLocation(location);
        file.delete();
    }
}
