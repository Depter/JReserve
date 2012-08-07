package org.jreserve.project.filesystem.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ClaimType;
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
public class ClaimTypeLoader implements ProjectElementFactory {
    
    private final static Comparator<ClaimType> COMPARATOR = new Comparator<ClaimType>() {
        @Override
        public int compare(ClaimType c1, ClaimType c2) {
            String n1 = c1.getName();
            String n2 = c2.getName();
            return n1.compareToIgnoreCase(n2);
        }
    };

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof LoB);
    }

    @Override
    public List<ProjectElement> createChildren(Object value, Session session) {
        List<ProjectElement> result = new ArrayList<ProjectElement>();
        for(ClaimType ct : getClaimTypes((LoB) value, session))
            result.add(getClaimTypeElement(ct, session));
        return result;
    }
    
    private List<ClaimType> getClaimTypes(LoB lob, Session session) {
        List<ClaimType> claimTypes = lob.getClaimTypes();
        Collections.sort(claimTypes, COMPARATOR);
        return claimTypes;
    }
    
    private ProjectElement getClaimTypeElement(ClaimType ct, Session session) {
        ProjectElement element = new ClaimTypeElement(ct);
        for(ProjectElement child : getChildren(ct, session))
            element.addChild(child);
        return element;
    }
    
    private List<ProjectElement> getChildren(ClaimType claimType, Session session) {
        List<ProjectElement> children = new ArrayList<ProjectElement>();
        for(ProjectElementFactory factory : RootElement.getFactories(claimType))
            children.addAll(factory.createChildren(claimType, session));
        return children;
    }
}
