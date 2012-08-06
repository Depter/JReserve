package org.jreserve.project.filesystem;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTypeLoader {
    
    private final static Comparator<ClaimType> LOB_COMPARATOR = new Comparator<ClaimType>() {
        @Override
        public int compare(ClaimType c1, ClaimType c2) {
            String n1 = c1.getName();
            String n2 = c2.getName();
            return n1.compareToIgnoreCase(n2);
        }
    };
    
    private static ClaimTypeLoader INSTANCE;
    
    public static ClaimTypeLoader getDefault() {
        if(INSTANCE == null)
            INSTANCE = new ClaimTypeLoader();
        return INSTANCE;
    }

    private final static Class CLAZZ = LoB.class;
    
    private final LookupListener listener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {
            refresh();
        }
    };
    
    private final Result<LoB> result;
    private final Set<LoB> loaded = new HashSet<LoB>();
    
    private ClaimTypeLoader() {
        result = ProjectFileSystem.getDefault().getLookup().lookupResult(CLAZZ);
        result.addLookupListener(listener);
        refresh();
    }
    
    private void refresh() {
        Collection<? extends LoB> lobs = result.allInstances();
        addNew(lobs);
    }
    
    private void addNew(Collection<? extends LoB> lobs) {
        for(LoB lob : lobs)
            if(!loaded.contains(lob))
                loadClaimTypes(lob);
    }
    
    private void loadClaimTypes(LoB lob) {
        loaded.add(lob);
        ProjectFileSystem fs = ProjectFileSystem.getDefault();
        ProjectElement parent = fs.getChildElement(lob);
        for(ClaimType claimType : lob.getClaimTypes())
            fs.addChild(parent, new ClaimTypeElement(claimType));
    }
}
