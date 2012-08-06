package org.jreserve.project.filesystem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.persistence.EntityLoader;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.LoB;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LoBLoader extends EntityLoader {
    
    private final static Comparator<LoB> LOB_COMPARATOR = new Comparator<LoB>() {
        @Override
        public int compare(LoB o1, LoB o2) {
            String n1 = o1.getName();
            String n2 = o2.getName();
            return n1.compareToIgnoreCase(n2);
        }
    };
    
    private static LoBLoader INSTANCE;
    
    public static LoBLoader getDefault() {
        if(INSTANCE == null)
            INSTANCE = new LoBLoader();
        return INSTANCE;
    }
    
    private LoBLoader() {
    }
    
    @Override
    protected void persistenceUnitClosed() {
        //TODO remove entities
    }

    @Override
    protected void persistenceUnitOpened(Session session) {
        List<LoB> lobs = session.getAll(LoB.class);
        Collections.sort(lobs, LOB_COMPARATOR);
        populateRoot(lobs);
    }
    
    
    private void populateRoot(List<LoB> lobs) {
        for(LoB lob : lobs)
            createLobFile(lob);
    }    
    
    private void createLobFile(LoB lob) {
        LoBElement element = new LoBElement(lob);
        ProjectFileSystem.getDefault().addChildToRoot(element);
    }
}
