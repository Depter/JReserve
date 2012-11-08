package org.jreserve.persistence.visual;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.hibernate.SessionFactory;
import org.jreserve.persistence.PersistenceUtil;
import org.netbeans.api.actions.Openable;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class PersistentOpenable implements Openable {
    
    private final static InstanceContent IC = new InstanceContent();
    public final static Lookup REGISTRY = new AbstractLookup(IC);
    private final static ConnectionListener CLOSER = new ConnectionListener(); 
    
    protected TopComponent component;
    private PropertyChangeListener listener;
    
    @Override
    public void open() {
        if(component == null)
            initComponent();
        if(!component.isOpened())
            component.open();
        component.requestActive();
        register();
    }

    private void initComponent() {
        component = createComponent();
        listener = WeakListeners.propertyChange(new TopComponentListener(), TopComponent.getRegistry());
        TopComponent.getRegistry().addPropertyChangeListener(listener);
    }
    
    protected abstract TopComponent createComponent();
    
    protected final void register() {
        IC.add(this);
    }
    
    public void close() {
        if(component != null)
            component.close();
    }
    
    protected final void unRegister() {
        IC.remove(this);
    }
    
    private class TopComponentListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(component != null && !component.isOpened() && isCloseEvent(evt)) {
                TopComponent.getRegistry().removePropertyChangeListener(listener);
                component = null;
                unRegister();
            }
        }
        
        private boolean isCloseEvent(PropertyChangeEvent evt) {
            String property = evt.getPropertyName();
            return TopComponent.Registry.PROP_TC_CLOSED.equalsIgnoreCase(property);
        }
    }
    
    private static class ConnectionListener implements LookupListener {
        
        private Result<SessionFactory> result;
        
        private ConnectionListener() {
            result = PersistenceUtil.getLookup().lookupResult(SessionFactory.class);
            result.addLookupListener(this);
        }

        @Override
        public void resultChanged(LookupEvent le) {
            if(result.allInstances().isEmpty())
                for(PersistentOpenable o : REGISTRY.lookupAll(PersistentOpenable.class))
                    o.close();
        }
        
    }
}
