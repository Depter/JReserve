package org.jreserve.project.entities.lob;

import org.jreserve.persistence.Session;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.Deletable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LoBElement extends ProjectElement<LoB> {
    
    LoBElement(LoB lob) {
        super(lob);
        super.addToLookup(new MyDeletable());
    }

    @Override
    public Node createNodeDelegate() {
        return new LoBNode(this);
    }
    
    private class MyDeletable implements Deletable {
        @Override
        public void delete() {
        }

        @Override
        public Node getNode() {
            return LoBElement.this.createNodeDelegate();
        }
    }
}
