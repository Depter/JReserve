package org.jreserve.project.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimDataPk implements Serializable {
    private final static long serialVersionUID = 1L;
    
    private long dataType;
    private long claimType;
    
    private Date accidentDate;
    private Date developmentDate;
    
    public ClaimDataPk() {
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public long getClaimType() {
        return claimType;
    }

    public long getDataType() {
        return dataType;
    }

    public Date getDevelopmentDate() {
        return developmentDate;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ClaimDataPk)
            return equals((ClaimDataPk) o);
        return false;
    }
    
    private boolean equals(ClaimDataPk o) {
        return claimType == o.claimType &&
               dataType == o.dataType &&
               accidentDate.equals(o.accidentDate) &&
               developmentDate.equals(o.developmentDate);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + (int) dataType;
        hash = 17 * hash + (int) claimType;
        hash = 17 * hash + accidentDate.hashCode();
        return 17 * hash + developmentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("ClaimDataPk [%d; %d; %tF; %tF]",
              dataType, claimType, accidentDate, developmentDate);
    }
}
