package org.jreserve.project.system.impl;

import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LoBElement extends ProjectElement {
    
    private LoB lob;
    
    LoBElement(LoB lob) {
        super(lob.getName());
        this.lob = lob;
    }

    @Override
    public Node createNodeDelegate() {
        return new LoBNode(this);
    }
    
    
}
