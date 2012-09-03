package org.jreserve.project.entities.lob;

import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.Deletable;
import org.jreserve.project.system.management.PersistentDeletable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LoBElement extends ProjectElement<LoB> {
    private final static Logger logger = Logging.getLogger(LoBElement.class.getName());
    
    LoBElement(LoB lob) {
        super(lob);
        super.addToLookup(new MyDeletable());
    }

    @Override
    public Node createNodeDelegate() {
        return new LoBNode(this);
    }
    
    private class MyDeletable extends PersistentDeletable {
        
        @Override
        public void delete(Session session) {
            LoB lob = getValue();
            logger.info("Deleting LoB: %s", lob);
            try {
                session.delete(lob);
            } catch(RuntimeException ex) {
                logger.error(ex, "Unable to delete LoB: %s", lob);
            }
        }

        @Override
        public Node getNode() {
            return LoBElement.this.createNodeDelegate();
        }
    }
}
