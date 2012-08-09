package org.jreserve.project.system.impl;

import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.system.ProjectElement;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimTypeElement extends ProjectElement {
    
    private ClaimType claimType;
    
    ClaimTypeElement(ClaimType claimType) {
        super(claimType.getName());
        this.claimType = claimType;
    }
    
    @Override
    public Node createNodeDelegate() {
        return new ClaimTypeNode(this);
    }
}
