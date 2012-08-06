package org.jreserve.project.filesystem;

import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.openide.filesystems.MIMEResolver;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@MIMEResolver.Registration(
    resource="EntityResolver.xml",
    displayName="#CTL_projectInstanceResolverName"
)
@Messages({
    "CTL_projectInstanceResolverName=Project Element"
})
public class ProjectFileSystem {
            
    private final static Logger logger = Logging.getLogger(ProjectFileSystem.class.getName());
    
    private static ProjectFileSystem INSTANCE;
    private static InstanceContent ic = new InstanceContent();
    private static Lookup lookup = new AbstractLookup(ic);
    
    public static ProjectFileSystem getDefault() {
        if(INSTANCE == null)
            INSTANCE = new ProjectFileSystem();
        return INSTANCE;
    }
    
    private ProjectFileSystem() {
    }
    
    public ProjectElement getRoot() {
        return ProjectElement.getRoot();
    }
    
    public ProjectElement getChildElement(Object... path) {
        return getChildElement(getRoot(), path);
    }
    
    public ProjectElement getChildElement(ProjectElement parent, Object... path) {
        for(int i=0, size=path.length; i<size; i++) {
            parent = getChildElement(parent, path[i]);
            if(parent == null)
                return null;
        }
        return parent;
    }
    
    public ProjectElement getChildElement(ProjectElement parent, Object entity) {
        for(ProjectElement element : parent.getChildren()) {
            EntityCookie ec = element.getCookie(EntityCookie.class);
            if(entity.equals(ec.getEntity()))
                return element;
        }
        return null;
    }
    
    public Lookup getLookup() {
        return lookup;
    }
    
    public void addChildToRoot(ProjectElement child) {
        addChild(getRoot(), child);
    }
    
    public void addChild(ProjectElement parent, ProjectElement child) {
        parent.addChild(child);
        if(isAttachedToRoot(parent))
            addEntitiesToLookup(child);
    }
    
    private boolean isAttachedToRoot(ProjectElement element) {
        while(element.getParent() != null)
            element = element.getParent();
        return element == getRoot();
    }
    
    private void addEntitiesToLookup(ProjectElement element) {
        EntityCookie cookie = element.getCookie(EntityCookie.class);
        addEntityCookieToLookup(cookie);
        for(ProjectElement child : element.getChildren())
            addEntitiesToLookup(child);
    }
    
    private void addEntityCookieToLookup(EntityCookie cookie) {
        if(cookie == null) return;
        Object entity = cookie.getEntity();
        if(entity!= null)
            ic.add(entity);
    }
    
    public void removeChildFromParent(ProjectElement element) {
        if(isAttachedToRoot(element))
            removeEntitiesFromLookup(element);
        element.removeFromParent();
    }
    
    private void removeEntitiesFromLookup(ProjectElement element) {
        EntityCookie cookie = element.getCookie(EntityCookie.class);
        removeEntityCookieFromLookup(cookie);
        for(ProjectElement child : element.getChildren())
            removeEntitiesFromLookup(child);
    }
    
    private void removeEntityCookieFromLookup(EntityCookie cookie) {
        if(cookie == null) return;
        Object entity = cookie.getEntity();
        if(entity!= null)
            ic.remove(entity);
    }
    
}
