package org.jreserve.project.entities.claimtype;

import javax.swing.SwingUtilities;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.RootElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimTypeElement extends ProjectElement<ClaimType> {
    
    private final static Logger logger = Logging.getLogger(ClaimTypeElement.class.getName());
    
    ClaimTypeElement(ClaimType claimType) {
        super(claimType);
        super.addToLookup(new ClaimTypeDeletable());
    }
    
    @Override
    public Node createNodeDelegate() {
        return new ClaimTypeNode(this);
    }
    
    private class ClaimTypeDeletable extends PersistentDeletable {

        @Override
        protected void delete(Session session) {
            ClaimType ct = getValue();
            logger.info("Deleting claim type: %s", ct);
            try {
                removeFromParent(ct);
                session.delete(ct);
            } catch(RuntimeException ex) {
                logger.error(ex, "Unable to delete claim type: %s", ct);
            }
        }

        private void removeFromParent(ClaimType ct) {
            LoB lob = ct.getLoB();
            removeElementFromParent(lob);
            lob.removeClaimType(ct);
        }
        
        private void removeElementFromParent(final LoB lob) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ProjectElement lobElement = RootElement.getDefault().getChild(lob);
                    lobElement.removeChild(ClaimTypeElement.this);
                }
            });
        }
        
        @Override
        public Node getNode() {
            return createNodeDelegate();
        }
    }
}
