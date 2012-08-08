package org.jreserve.project.system;

import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectElement implements Lookup.Provider {
    
    private final static String PATH_SEPARATOR = "/";
    
    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new AbstractLookup(ic);
    
    private boolean isLoaded = false;
    private ProjectElement parent;
    private List<ProjectElement> children = new ArrayList<ProjectElement>();
    private List<ProjectElementListener> listeners = new ArrayList<ProjectElementListener>();
    
    private Object value;

    private ProjectElement() {
    }
    
    public ProjectElement(Object value) {
        this.value = value;
    }
    
    public Object getValue() {
        return value;
    }
    
    boolean isLoaded() {
        return isLoaded;
    }
    
    void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    protected void addToLookup(Object o) {
        ic.add(o);
    }
    
    protected void removeFromLookup(Object o) {
        ic.remove(o);
    }
    
    public Node createNodeDelegate() {
        return new DefaultProjectNode(this);
    }
    
    public ProjectElement getParent() {
        return parent;
    }
    
    public List<ProjectElement> getChildren() {
        return new ArrayList<ProjectElement>(children);
    }
    
    public void addChild(ProjectElement child) {
        checkNewChild(child);
        child.setParent(this);
        children.add(child);
        fireChildAdded(child);
    }
    
    void setParent(ProjectElement parent) {
        this.parent = parent;
    }
    
    private void checkNewChild(ProjectElement child) {
        if(child == null)
            throw new NullPointerException("Child is null!");
        if(child.getParent() != null)
            throw new IllegalArgumentException(String.format("Child '%s' already added to element '%s'", child.getValue(), child.getParent()));
        checkNewName(child);
    }
    
    private void checkNewName(ProjectElement child) {
        if(!valueExists(child.getValue())) 
            return;
        String msg = String.format("Value '%s' already exists in element '%s'!", child.getValue(), this);
        throw new IllegalArgumentException(msg);
    }
    
    public boolean valueExists(Object value) {
        for(ProjectElement element : children)
            if(valueEquals(value, element.getValue()))
                return true;
        return false;
    }
    
    private boolean valueEquals(Object v1, Object v2) {
        if(v1 == null)
            return v2 == null;
        if(v1 instanceof String)
            return (v2 instanceof String)? ((String)v1).equalsIgnoreCase((String) v2) : false;
        return v1.equals(v2);
    }
    
    private void fireChildAdded(ProjectElement child) {
        for(ProjectElementListener listener : new ArrayList<ProjectElementListener>(listeners))
            listener.childAdded(child);
    }
    
    public void removeChild(ProjectElement child) {
        checkMyChild(child);
        child.setParent(null);
        children.remove(child);
        fireChildRemoved(child);
    }
    
    private void checkMyChild(ProjectElement child) {
        if(child == null)
            throw new NullPointerException("Child is null!");
        if(this == child.getParent())
            return;
        String msg = "Child '%s' is not added to element '%s'!";
        throw new IllegalArgumentException(String.format(msg, child, this));
    }
    
    private void fireChildRemoved(ProjectElement child) {
        for(ProjectElementListener listener : new ArrayList<ProjectElementListener>(listeners))
            listener.childRemoved(child);
    }
    
    public void removeFromParent() {
        if(parent != null)
            parent.removeChild(this);
    }
    
    public void addProjectElementListener(ProjectElementListener listener) {
        if(listener==null || listeners.contains(listener))
            return;
        listeners.add(listener);
    }
    
    public void removeProjectElementListener(ProjectElementListener listener) {
        listeners.remove(listener);
    }
    
    public String getPath() {
        if(parent == null)
            return "";
        String name = value==null? "null" : value.toString();
        return parent.getPath()+PATH_SEPARATOR+name;
    }
    
    @Override
    public String toString() {
        return getPath();
    }
}
