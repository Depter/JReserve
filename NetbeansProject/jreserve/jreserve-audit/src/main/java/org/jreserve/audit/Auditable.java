package org.jreserve.audit;

/**
 *
 * @author Peter Decsi
 */
public interface Auditable {

    public Object getAuditableEntity();
    
    public String getDisplayName();
}
