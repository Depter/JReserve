package org.jreserve.rutil;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface RFunction {

    public String getName();
    
    public String getDescription();
    
    public Map<String, String> getParameters();
    
    public String getReturn();
    
    public String getSource();
    
    public Set<String> getFunctionDependendencies(); 
    
    public Set<String> getLibraryDependendencies();
}
