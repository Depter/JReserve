package org.jreserve.project.entities.claimtype;

import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.MultiPersistentSavable;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimTypeElement extends ProjectElement<ClaimType> {
    
    ClaimTypeElement(ClaimType claimType) {
        super(claimType);
        properties.put(NAME_PROPERTY, claimType.getName());
        super.addToLookup(new ClaimTypeDeletable());
        super.addToLookup(new RenameableProjectElement(this));
    }
    
    @Override
    public Node createNodeDelegate() {
        return new ClaimTypeNode(this);
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            setName((String) value);
        super.setProperty(property, value);
        getValue().setName((String) value);
    }
    
    private void setName(String name) {
        getValue().setName(name);
        addToLookup(new MultiPersistentSavable(this));
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