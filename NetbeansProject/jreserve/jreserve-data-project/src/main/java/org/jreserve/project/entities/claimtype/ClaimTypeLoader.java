package org.jreserve.project.entities.claimtype;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    public boolean isInterested(ProjectElement parent) {
        return parent != null &&
               (parent.getValue() instanceof LoB);
    }

    @Override
    protected List<ClaimType> getChildValues(ProjectElement parent) {
        List<ClaimType> claimTypes = ((LoB)parent.getValue()).getClaimTypes();
        Collections.sort(claimTypes, COMPARATOR);
        return claimTypes;
    }
    
    @Override
    protected ProjectElement createProjectElement(ClaimType claimType) {
        return new ClaimTypeElement(claimType);
    }
}
