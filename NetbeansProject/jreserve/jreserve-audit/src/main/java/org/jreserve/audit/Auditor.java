package org.jreserve.audit;

import java.util.List;
import org.hibernate.envers.AuditReader;

/**
 *
 * @author Peter Decsi
 */
public interface Auditor {

    public boolean isInterested(Object value);
    
    public List<AuditElement> getAudits(AuditReader reader, Object value);
    
    public @interface Registration {
        public int value() default Integer.MAX_VALUE;
    }
}