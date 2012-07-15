package org.jreserve.data.base;

import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataSource {
    
    public List<LoB> getLoBs();
    
    public List<DataType> getDataTypes();
    
    public Double getValue(DataParameters parameters);
}
