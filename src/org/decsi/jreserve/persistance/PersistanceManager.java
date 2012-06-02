package org.decsi.jreserve.persistance;

import java.util.List;
import org.decsi.jreserve.data.LoB;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface PersistanceManager {
    
    public List<LoB> getLoBs();
}
