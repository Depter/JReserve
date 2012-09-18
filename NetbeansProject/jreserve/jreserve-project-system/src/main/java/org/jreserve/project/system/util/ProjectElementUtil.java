package org.jreserve.project.system.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemCreationListener;
import org.jreserve.project.system.management.ProjectSystemDeletionListener;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 */
public class ProjectElementUtil {

    public static void created(ProjectElement element) {
        for(ProjectSystemCreationListener listener : getListeners(ProjectSystemCreationListener.class, element.getValue()))
            listener.created(element);
    }
    
    private static <T extends ProjectSystemListener> List<T> getListeners(Class<T> clazz, Object value) {
        List<T> listeners = lookupListeners(clazz);
        for(Iterator<T> it = listeners.iterator(); it.hasNext();)
            if(!it.next().isInterested(value))
                it.remove();
        return listeners;
    }
    
    private static <T> List<T> lookupListeners(Class<T> clazz) {
        return new ArrayList<T>(Lookup.getDefault().lookupAll(clazz));
    }
    
    public static void deleted(ProjectElement parent, ProjectElement child) {
        for(ProjectSystemDeletionListener listener : getListeners(ProjectSystemDeletionListener.class, child.getValue()))
            listener.deleted(parent, child);
    }
    
    private ProjectElementUtil() {
    }
}
