package org.jreserve.project.entities.lob;

import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LoBElement extends ProjectElement<LoB> {
    
    LoBElement(LoB lob) {
        super(lob);
        setProperty(NAME_PROPERTY, lob.getName());
        super.addToLookup(new LoBDeletable());
    }

    @Override
    public Node createNodeDelegate() {
        return new LoBNode(this);
    }
    
    private class LoBDeletable extends PersistentDeletable {
        
        private LoBDeletable() {
            super(LoBElement.this);
        }
        
        @Override
        protected void cleanUpEntity() {
        }
        
        @Override
        public Node getNode() {
            return LoBElement.this.createNodeDelegate();
        }
    }
}
