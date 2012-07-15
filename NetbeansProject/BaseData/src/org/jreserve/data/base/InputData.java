package org.jreserve.data.base;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface InputData {
    
    public ClaimType getClaimType();
    
    public DataType getDataType();
    
    public Date getAccidentDate();
    
    public Date getDevelopmentDate();
    
    public double getValue();
}
