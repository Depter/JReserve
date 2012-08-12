package org.jreserve.project.system.management;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Decsi
 */
public abstract class Deletable {
    
    private final static InstanceContent REGISTRY_CONTENT = new InstanceContent();
    public final static Lookup REGISTRY = new AbstractLookup(REGISTRY_CONTENT);
    
    public abstract void delete();
    
    protected void register() {
        REGISTRY_CONTENT.add(this);
    }
    
    protected void unregister() {
        REGISTRY_CONTENT.remove(this);
    }
}
