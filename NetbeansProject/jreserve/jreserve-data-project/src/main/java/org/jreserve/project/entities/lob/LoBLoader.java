package org.jreserve.project.entities.lob;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.hibernate.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration(ProjectElementFactory.MAX_PRIORITY)
public class LoBLoader extends AbstractProjectElementFactory<LoB> {
    
    private final static Comparator<LoB> LOB_COMPARATOR = new Comparator<LoB>() {
        @Override
        public int compare(LoB o1, LoB o2) {
            String n1 = o1.getName();
            String n2 = o2.getName();
            return n1.compareToIgnoreCase(n2);
        }
    };

    @Override
    public boolean isInterested(ProjectElement parent) {
        return parent == null;
    }

    @Override
    protected List<LoB> getChildValues(ProjectElement parent) {
        List<LoB> lobs = loadLobs();
        Collections.sort(lobs, LOB_COMPARATOR);
        return lobs;
    }
    
    private List<LoB> loadLobs() {
        Session session = SessionFactory.getCurrentSession();
        return session.createQuery("from LoB").list();
    }
    
    @Override
    protected ProjectElement createProjectElement(LoB value) {
        return new LoBElement(value);
    }
}
