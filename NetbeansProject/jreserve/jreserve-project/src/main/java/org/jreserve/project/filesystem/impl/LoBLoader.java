package org.jreserve.project.filesystem.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;
import org.jreserve.project.system.RootElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration(ProjectElementFactory.MAX_PRIORITY)
public class LoBLoader implements ProjectElementFactory {
    
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
    public List<ProjectElement> createChildren(Object value, Session session) {
        List<ProjectElement> result = new ArrayList<ProjectElement>();
        for(LoB lob : getLoBs(session))
            result.add(getLoBElement(lob, session));
        return result;
    }
    
    private List<LoB> getLoBs(Session session) {
        List<LoB> lobs = session.getAll(LoB.class);
        Collections.sort(lobs, LOB_COMPARATOR);
        return lobs;
    }
    
    private ProjectElement getLoBElement(LoB lob, Session session) {
        ProjectElement element = new LoBElement(lob);
        for(ProjectElement child : getChildren(lob, session))
            element.addChild(child);
        return element;
    }
    
    private List<ProjectElement> getChildren(LoB lob, Session session) {
        List<ProjectElement> children = new ArrayList<ProjectElement>();
        for(ProjectElementFactory factory : RootElement.getFactories(lob))
            children.addAll(factory.createChildren(lob, session));
        return children;
    }
}
