package org.jreserve.project.system.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ClaimType;
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
public class ClaimTypeLoader extends AbstractProjectElementFactory<ClaimType> {
    
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
    protected List<ClaimType> getChildValues(Object value, Session session) {
        List<ClaimType> claimTypes = ((LoB)value).getClaimTypes();
        Collections.sort(claimTypes, COMPARATOR);
        return claimTypes;
    }
    
    @Override
    protected ProjectElement createProjectElement(ClaimType claimType) {
        return new ClaimTypeElement(claimType);
    }
}
