package org.jreserve.project.filesystem;

import org.jreserve.project.entities.ClaimType;

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
        super.getCookieSet().add(new EntityCookieImpl<ClaimType>(claimType));
    }

}
