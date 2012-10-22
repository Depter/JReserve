package org.jreserve.audit;

import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 */
public class AuditableProjectElement implements Auditable {
    
    private ProjectElement element;
    
    public AuditableProjectElement(ProjectElement element) {
        this.element = element;
    }

    @Override
    public Object getAuditableEntity() {
        return element.getValue();
    }

    @Override
    public String getDisplayName() {
        return element.getNamePath();
    }
}
