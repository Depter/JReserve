package org.jreserve.project.system.filesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProjectFile implements Comparable<ProjectFile> {
    
    static ProjectFile createRoot() {
        ProjectFile root = new ProjectFile("");
        root.name = null;
        return root;
    }
    
    private String name;
    private ProjectFile parent;
    private List<ProjectFile> children = new ArrayList<ProjectFile>();
    private Map<String, Object> attributes = new HashMap<String, Object>();
    
    private ProjectFile(String name) {
        this.name = name;
    }
    
    String getName() {
        return name;
    }
    
    void setName(String name) {
        if(name == null)
            throw new NullPointerException("Name can not be null!");
        if(this.name == null)
            throw new UnsupportedOperationException("Root element can not be renamed!");
        this.name = name;
    }
    
    boolean isFolder() {
        return true;
    }

    ProjectFile getParent() {
        return parent;
    }
    
    List<ProjectFile> getChildren() {
        return children;
    }
    
    List<String> getAttributeNames() {
        return new ArrayList<String>(attributes.keySet());
    }
    
    Object getAttribute(String name) {
        return attributes.get(name);
    }
    
    void setAttribute(String name, Object value) {
        if(value == null)
            attributes.remove(name);
        else
            attributes.put(name, value);
    }
    
    void clearAttributes() {
        attributes.clear();
    }
    
    ProjectFile createChild(String name) throws IOException {
        checkName(name);
        ProjectFile child = new ProjectFile(name);
        addChild(child);
        return child;
    }
    
    private void checkName(String name) throws IOException {
        if(name == null)
            throw new IOException("Null name not allowed!");
        for(ProjectFile child : children)
            if(child.getName().equalsIgnoreCase(name))
                throw new IOException("Name '"+name+" ' already exists in folder '"+this+"'!");
    }
    
    void addChild(ProjectFile child) {
        if(child.parent != null)
            throw childOwnedException(child);
        if(child.name == null)
            throw rootException();
        child.parent = this;
        children.add(getIndex(child.name), child);
    }
    
    void delete() {
        if(parent == null)
            return;
        parent.children.remove(this);
        parent = null;
    }
    
    private IllegalArgumentException childOwnedException(ProjectFile element) {
        String msg = "ProjectElement %s can not be added to '%s' because it is already added to '%s'";
        msg = String.format(msg, element.name, this, element.parent);
        return new IllegalArgumentException(msg);
    }
    
    private IllegalArgumentException rootException() {
        String msg = "Root can not be added to '%s'!";
        msg = String.format(msg, this);
        return new IllegalArgumentException(msg);
    }
    
    private int getIndex(String name) {
        for(int i=0, length=children.size(); i<length; i++)
            if(name.compareToIgnoreCase(children.get(i).name) < 0)
                return i;
        return children.size();
    }
    
    @Override
    public int compareTo(ProjectFile e) {
        if(e == null)
            return -1;
        if(name == null)
            return e.name==null? 0 : -1;
        return e.name==null? 1 : name.compareToIgnoreCase(e.name);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof ProjectFile)
            return compareTo((ProjectFile) o) == 0;
        return false;
    }
    
    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
    
    String getPath() {
        if(parent == null)
            return "";
        if(parent.parent == null)
            return name;
        return parent.getPath() + ProjectFileSystem.PATH_SEPARATOR + name;
    }
    
    @Override
    public String toString() {
        return getPath();
    }
}
