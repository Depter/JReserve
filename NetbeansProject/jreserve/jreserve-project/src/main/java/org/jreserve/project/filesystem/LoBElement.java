package org.jreserve.project.filesystem;

import org.jreserve.project.entities.LoB;

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
        super.getCookieSet().add(new EntityCookieImpl<LoB>(lob));
    }
    
    
}
