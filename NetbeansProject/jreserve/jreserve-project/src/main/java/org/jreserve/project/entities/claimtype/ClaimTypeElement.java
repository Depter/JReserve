package org.jreserve.project.entities.claimtype;

import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimTypeElement extends ProjectElement<ClaimType> {
    
    ClaimTypeElement(ClaimType claimType) {
        super(claimType);
        setProperty(NAME_PROPERTY, claimType.getName());
        super.addToLookup(new ClaimTypeDeletable());
    }
    
    @Override
    public Node createNodeDelegate() {
        return new ClaimTypeNode(this);
    }
    
    private class ClaimTypeDeletable extends PersistentDeletable {

        private ClaimTypeDeletable() {
            super(ClaimTypeElement.this);
        }
        
        @Override
        protected void cleanUpEntity() {
            ClaimType ct = getValue();
            LoB lob = ct.getLoB();
            lob.removeClaimType(ct);
        }
        
        @Override
        public Node getNode() {
            return createNodeDelegate();
        }
    }
}