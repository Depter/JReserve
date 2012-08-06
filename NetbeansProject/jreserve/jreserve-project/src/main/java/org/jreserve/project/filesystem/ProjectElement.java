package org.jreserve.project.filesystem;

import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectElement implements Lookup.Provider {
    
    private final static ProjectElement ROOT = new ProjectElement();
    public static ProjectElement getRoot() {
        return ROOT;
    }
    
    private final static String PATH_SEPARATOR = "/";
    
    private CookieSet cookies = new CookieSet();
    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new ProxyLookup(Lookups.proxy(cookies), new AbstractLookup(ic));
    
    private ProjectElement parent;
    private List<ProjectElement> children = new ArrayList<ProjectElement>();
    private List<ProjectElementListener> listeners = new ArrayList<ProjectElementListener>();
    
    private String name;

    private ProjectElement() {
    }
    
    public ProjectElement(String name) {
        this.name = name;
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    public CookieSet getCookieSet() {
        return cookies;
    }
    
    public <E extends Node.Cookie> E getCookie(Class<E> clazz) {
        return cookies.getCookie(clazz);
    }
    
    public String getName() {
        return name;
    }
    
    public List<ProjectElement> getChildren() {
        return new ArrayList<ProjectElement>(children);
    }
    
    public Node createNodeDelegate() {
        return new DefaultProjectNode(this);
    }
    
    public ProjectElement getParent() {
        return parent;
    }
    
    public void addChild(ProjectElement child) {
        checkNewChild(child);
        child.parent = this;
        children.add(child);
        fireChildAdded(child);
    }
    
    private void checkNewChild(ProjectElement child) {
        if(child == null)
            throw new NullPointerException("Child is null!");
        if(child.getParent() != null)
            throw new IllegalArgumentException(String.format("Child '%s' already added to element '%s'", child.getName(), child.getParent()));
        checkNewName(child);
    }
    
    private void checkNewName(ProjectElement child) {
        if(!nameExists(child.getName())) 
            return;
        String msg = String.format("Name '%s' already exists in element '%s'!", child.getName(), this);
        throw new IllegalArgumentException(msg);
    }
    
    public boolean nameExists(String name) {
        for(ProjectElement element : children)
            if(name.equalsIgnoreCase(element.getName()))
                return true;
        return false;
    }
    
    private void fireChildAdded(ProjectElement child) {
        for(ProjectElementListener listener : new ArrayList<ProjectElementListener>(listeners))
            listener.childAdded(child);
    }
    
    public void removeChild(ProjectElement child) {
        checkMyChild(child);
        child.parent = null;
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
        return parent.getPath()+PATH_SEPARATOR+getName();
    }
    
    @Override
    public String toString() {
        return getPath();
    }
}
