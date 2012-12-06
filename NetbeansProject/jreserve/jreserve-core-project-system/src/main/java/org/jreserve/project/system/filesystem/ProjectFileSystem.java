package org.jreserve.project.system.filesystem;

import org.openide.filesystems.AbstractFileSystem;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectFileSystem extends AbstractFileSystem {
    
    private static ProjectFileSystem DEFAULT = null;
    public static ProjectFileSystem getDefault() {
        if(DEFAULT == null)
            DEFAULT = new ProjectFileSystem();
        return DEFAULT;
    } 
    
    final static String MIME_TYPE = "MIME_TYPE";
    final static String PATH_SEPARATOR = "/";
    
    static String getName(String location) {
        int index = location.lastIndexOf(PATH_SEPARATOR);
        if(index < 0)
            return location;
        return location.substring(index+1);
    }
    
    static String[] getPath(String location) {
        return location.split(PATH_SEPARATOR);
    }
    
    private ProjectFile root = ProjectFile.createRoot();
    
    ProjectFileSystem() {
        super.list = new PFSList(this);
        super.change = new PFSChange(this);
        super.attr = new PFSAttr(this);
        super.info = new PFSInfo(this);
    }
    
    @Override
    public String getDisplayName() {
        return "Project File System";
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
    
    ProjectFile getFileForLocation(String location) {
        if(location==null || location.length()==0)
            return root;
        if(location.startsWith(PATH_SEPARATOR))
            return getFileForLocation(location.substring(1));
        String[] path = location.split(PATH_SEPARATOR);
        return getFileForPath(path);
    }
    
    ProjectFile getFileForPath(String[] path) {
        ProjectFile file = root;
        for(String name : path)
            file = getChild(file, name);
        return file;
    }
        
    private ProjectFile getChild(ProjectFile file, String name) {
        for(ProjectFile child : file.getChildren())
            if(child.getName().equals(name))
                return child;
        throw new IllegalArgumentException("Child '"+name+"' not found in element: "+file);
    }
}

