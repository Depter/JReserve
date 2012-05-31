package org.decsi.jreserve.database;

import java.util.List;
import org.decsi.jreserve.data.ClaimType;
import org.decsi.jreserve.data.LoB;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Database {

    public List<LoB> getLoBs();
    
    public List<ClaimType> getClaimTypes(LoB lob);
    
    
}
