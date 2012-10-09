package org.jreserve.data.dataexplorer;

import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.project.entities.ClaimType;
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
    
    private static Map<String, DataExplorerTopComponent> explorers = new HashMap<String, DataExplorerTopComponent>();
    
    public static DataExplorerTopComponent getComponent(ProjectElement<ClaimType> element) {
        ClaimType claimType = element.getValue();
        DataExplorerTopComponent explorer = explorers.get(claimType.getId());
        if(explorer == null) {
            explorer = new DataExplorerTopComponent(element);
            explorers.put(claimType.getId(), explorer);
        }
        return explorer;
    }
    
    static void removeComponent(ClaimType claimType) {
        explorers.remove(claimType.getId());
    }
    
    static void closeIfExists(final ClaimType claimType) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DataExplorerTopComponent component = explorers.get(claimType.getId());
                if(component != null)
                    component.close();
            }
        });
    }
    
    private DataExplorerRegistry() {
    }
}
