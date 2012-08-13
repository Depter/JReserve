package org.jreserve.project.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * This class is the basic element in representing data structure within the
 * application.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectElement<T> implements Lookup.Provider {
    
    private final static String PATH_SEPARATOR = "/";
    
    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new AbstractLookup(ic);
    
    private boolean isLoaded = false;
    private ProjectElement parent;
    private List<ProjectElement> children = new ArrayList<ProjectElement>();
    private List<ProjectElementListener> listeners = new ArrayList<ProjectElementListener>();
    
    private T value;
    
    /**
     * Creates an instance with the given value. This value is added to the lookup.
     */
    public ProjectElement(T value) {
        if(value == null)
            throw new NullPointerException("Null value not allowed!");
        this.value = value;
        ic.add(value);
    }
    
    /**
     * Returns the value, this element represents.
     */
    public T getValue() {
        return value;
    }
    
    boolean isLoaded() {
        return isLoaded;
    }
    
    void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }
    
    /**
     * Returns the lookup for this element.
     */
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
    
    public <T> List<ProjectElement> getChildren(Class<T> valueClass) {
        List<ProjectElement> result = new ArrayList<ProjectElement>();
        for(ProjectElement child : children) {
            Class clazz = child.getValue().getClass();
            if(valueClass.isAssignableFrom(clazz))
                result.add(child);
        }
        return result;
    }
    
    public <T> List<T> getChildValues(Class<T> clazz) {
        List result = new ArrayList();
        for(ProjectElement child : children) {
            Object childValue = child.getValue();
            if(clazz.isAssignableFrom(childValue.getClass()))
                result.add(childValue);
        }
        return result;
    }
    
    public ProjectElement getChild(Object value) {
        for(ProjectElement child : children)
            if(child.getValue().equals(value))
                return child;
        return null;
    }
    
    public void addChild(ProjectElement child) {
        addChild(children.size(), child);
    }
    
    public void addChild(int index, ProjectElement child) {
        checkNewChild(child);
        child.setParent(this);
        children.add(index, child);
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
        for(ProjectElement child : children)
            if(valueEquals(value, child.getValue()))
                return true;
        return false;
    }
    
    private boolean valueEquals(Object v1, Object v2) {
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
    
    public void setChildren(List<ProjectElement> newChildren) {
        checkNewChildren(newChildren);
        removeAllChildren();
        addAllChildren(newChildren);
        fireChildrenChanged();
    }
    
    private void checkNewChildren(List<ProjectElement> newChildren) {
        for(ProjectElement element : newChildren)
            checkNewChild(element);
    }
    
    private void removeAllChildren() {
        for(Iterator<ProjectElement> it = children.iterator(); it.hasNext();) {
            it.next().setParent(null);
            it.remove();
        }
    }

    private void addAllChildren(List<ProjectElement> newChildren) {
        for(ProjectElement element : newChildren) {
            element.setParent(this);
            children.add(element);
        }
    }
    
    private void fireChildrenChanged() {
        for(ProjectElementListener listener : new ArrayList<ProjectElementListener>(listeners))
            listener.childrenChanged();
    }
    
    @Override
    public String toString() {
        return getPath();
    }
    
    public String getPath() {
        if(parent == null)
            return "";
        String name = value==null? "null" : value.toString();
        return parent.getPath()+PATH_SEPARATOR+name;
    }
}
