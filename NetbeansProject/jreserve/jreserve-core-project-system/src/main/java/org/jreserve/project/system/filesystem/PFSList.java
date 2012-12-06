package org.jreserve.project.system.filesystem;

import org.openide.filesystems.AbstractFileSystem;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PFSList  implements AbstractFileSystem.List {

    private final ProjectFileSystem fs;
    
    PFSList(ProjectFileSystem fs) {
        this.fs = fs;
    }
    
    @Override
    public String[] children(String location) {
        ProjectFile parent = fs.getFileForLocation(location);
        return getFileNames(parent.getChildren());
    }
        
    private String[] getFileNames(java.util.List<ProjectFile> files) {
        String[] names = new String[files.size()];
        for(int i=0, length=files.size(); i<length; i++)
            names[i] = files.get(i).getName();
        return names;
    }
}
