package org.jreserve.data.dataexplorer;

import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Peter Decsi
 */
class DataExplorerRegistry {
    
    private final static LookupListener PU_LISTENER = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {
            if(PU_RESULT.allInstances().isEmpty())
                closeExplorers();
        }
    };
    
    private final static Result<PersistenceUnit> PU_RESULT = PersistenceUtil.getLookup().lookupResult(PersistenceUnit.class);
    static {
        PU_RESULT.addLookupListener(PU_LISTENER);
    }

    private static void closeExplorers() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for(DataExplorerTopComponent component : explorers.values())
                    component.close();
            }
        });
        
    }
    
    private static Map<Long, DataExplorerTopComponent> explorers = new HashMap<Long, DataExplorerTopComponent>();
    
    public static DataExplorerTopComponent getComponent(ProjectElement<Project> element) {
        Project project = element.getValue();
        DataExplorerTopComponent explorer = explorers.get(project.getId());
        if(explorer == null) {
            explorer = new DataExplorerTopComponent(element);
            explorers.put(project.getId(), explorer);
        }
        return explorer;
    }
    
    static void removeComponent(Project project) {
        explorers.remove(project.getId());
    }
    
    static void closeIfExists(final Project project) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DataExplorerTopComponent component = explorers.get(project.getId());
                if(component != null)
                    component.close();
            }
        });
    }
    
    private DataExplorerRegistry() {
    }
}
