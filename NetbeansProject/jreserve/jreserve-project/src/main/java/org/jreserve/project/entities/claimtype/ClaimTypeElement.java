package org.jreserve.project.entities.claimtype;

import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.system.ProjectElement;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimTypeElement extends ProjectElement<ClaimType> {
    
    ClaimTypeElement(ClaimType claimType) {
        super(claimType);
    }
    
    @Override
    public Node createNodeDelegate() {
        return new ClaimTypeNode(this);
    }
}
