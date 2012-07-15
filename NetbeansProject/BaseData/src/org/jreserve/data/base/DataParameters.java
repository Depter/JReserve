package org.jreserve.data.base;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataParameters {
    
    public ClaimType getClaimType();
    
    public DataType getDataType();
    
    public Date getAccidentStartDate();
    
    public Date getAccidentEndDate();
    
    public Date getDevelopmentStartDate();
    
    public Date getDevelopmentEndDate();
    
}
