package org.jreserve.audit.global;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Peter Decsi
 */
class RefreshAction extends AbstractAction {

    private final AuditedEntityNode context;
    
    public RefreshAction(AuditedEntityNode context) {
        this.context = context;
        super.putValue(NAME, "Refresh");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        context.refreshChildren();
    }

}
