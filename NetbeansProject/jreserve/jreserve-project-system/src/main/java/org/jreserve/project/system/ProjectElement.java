package org.jreserve.project.system;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
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
    
    public final static String NAME_PROPERTY = "ELEMENT_NAME";
    private final static String PATH_SEPARATOR = "/";
    
    
    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new AbstractLookup(ic);
    private Properties properties = new Properties();
    private List<PropertyChangeListener> propertyListeners = new ArrayList<PropertyChangeListener>();
    
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
        ic.add(this);
    }
    
    public void setProperty(String property, Object value) {
        Object oldValue = properties.get(property);
        if(value == null)
            properties.remove(property);
        else
            properties.put(property, value);
        firePropertyChange(property, oldValue, value);
    }
    
    private void firePropertyChange(String property, Object oldValue, Object newValue) {
        PropertyChangeEvent evt = new PropertyChangeEvent(this, property, oldValue, newValue);
        for(PropertyChangeListener l : new ArrayList<PropertyChangeListener>(propertyListeners))
            l.propertyChange(evt);
    }
    
    public Object getProperty(String property) {
        return properties.get(property);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if(!propertyListeners.contains(listener))
            propertyListeners.add(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyListeners.remove(listener);
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
    
    /**
     * Adds the given object to the lookup. 
     * The object can not be null!
     */
    protected void addToLookup(Object o) {
        if(o == null)
            throw new NullPointerException("Can not add null value to the lookup!");
        ic.add(o);
    }
    
    /**
     * Removes the given object from the lookup.
     */
    protected void removeFromLookup(Object o) {
        ic.remove(o);
    }
    
    /**
     * Returns a {@link org.openide.nodes.Node Node} representing this
     * project element. If not overriden, this method will return a
     * {@link org.jreserve.project.system.DefaultProjectNode DefaultProjectNode}.
     */
    public Node createNodeDelegate() {
        return new DefaultProjectNode(this);
    }
    
    /**
     * Returns the parent of this element, or null, if there is no
     * parent element.
     */
    public ProjectElement getParent() {
        return parent;
    }
    
    /**
     * Returns the list of child elements. Modifying the
     * returned list does not affect this element.
     */
    public List<ProjectElement> getChildren() {
        return new ArrayList<ProjectElement>(children);
    }
    
    /**
     * Returns a list of children, that contain values from the
     * given class.
     */
    public <T> List<ProjectElement> getChildren(Class<T> valueClass) {
        List<ProjectElement> result = new ArrayList<ProjectElement>();
        for(ProjectElement child : children) {
            Class clazz = child.getValue().getClass();
            if(valueClass.isAssignableFrom(clazz))
                result.add(child);
        }
        return result;
    }
    
    /**
     * Returns the values from the children of this element, that
     * contain a value with the given class.
     */
    public <T> List<T> getChildValues(Class<T> clazz) {
        List result = new ArrayList();
        for(ProjectElement child : children) {
            Object childValue = child.getValue();
            if(clazz.isAssignableFrom(childValue.getClass()))
                result.add(childValue);
        }
        return result;
    }
    
    /**
     * Returns the child of this element, that contains the 
     * given value. If no such child exists, then <i>null</i>
     * will be returned.
     */
    public ProjectElement getChild(Object value) {
        for(ProjectElement child : children)
            if(child.getValue().equals(value))
                return child;
        return null;
    }
    
    /**
     * Adds the given child to the end of the childrens list.
     */
    public void addChild(ProjectElement child) {
        addChild(children.size(), child);
    }
    
    /**
     * Adds the given child to the given position.
     * 
     * @param index the index at which the new element should be added. Must
     *              be less than 0 or greater than the number of children.
     * @param child The child to add. Must not be <i>null</i>, and most not
     *              have a parent.
     */
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
    
    /**
     * Returns <b>true</b> if one of the children contains
     * the given value.
     */
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
    
    /**
     * Removes the given child. The given child must not be <i>null</i>
     * and has to be the child of this instance.
     */
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
    
    /**
     * Removes this element from it's parent if it has a parent, 
     * otherwise nothing happens.
     */
    public void removeFromParent() {
        if(parent != null)
            parent.removeChild(this);
    }
    
    /**
     * Adds a listener to this element.
     */
    public void addProjectElementListener(ProjectElementListener listener) {
        if(listener==null || listeners.contains(listener))
            return;
        listeners.add(listener);
    }
    
    /**
     * Removes the listener from this element.
     */
    public void removeProjectElementListener(ProjectElementListener listener) {
        listeners.remove(listener);
    }
    
    void setChildren(List<ProjectElement> newChildren) {
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
    
    private String getPath() {
        if(parent == null)
            return "";
        String name = value==null? "null" : value.toString();
        return parent.getPath()+PATH_SEPARATOR+name;
    }
}
