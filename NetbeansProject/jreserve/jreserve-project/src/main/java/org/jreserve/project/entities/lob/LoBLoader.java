package org.jreserve.project.entities.lob;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;
import org.jreserve.project.system.RootElement;

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
    public boolean isInterested(Object value) {
        return (value instanceof RootElement.RootValue);
    }

    @Override
    protected List<LoB> getChildValues(Object value, Session session) {
        List<LoB> lobs = session.getAll(LoB.class);
        Collections.sort(lobs, LOB_COMPARATOR);
        return lobs;
    }
    
    @Override
    protected ProjectElement createProjectElement(LoB value) {
        return new LoBElement(value);
    }
}
