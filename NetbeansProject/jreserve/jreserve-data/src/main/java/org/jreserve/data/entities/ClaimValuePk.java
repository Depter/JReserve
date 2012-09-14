package org.jreserve.data.entities;

import java.io.Serializable;
import java.util.Date;
import org.jreserve.data.Data;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimValuePk implements Serializable {
    private final static long serialVersionUID = 1L;
    
    private int dataTypeId;
    private long claimType;
    
    private Date accidentDate;
    private Date developmentDate;
    
    public ClaimValuePk() {
    }
    
    public ClaimValuePk(ClaimType ct, Data data) {
        this.claimType = ct.getId();
        this.dataTypeId = data.getDataType().getDbId();
        this.accidentDate = data.getAccidentDate();
        this.developmentDate = data.getDevelopmentDate();
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public long getClaimType() {
        return claimType;
    }

    public int getDataTypeId() {
        return dataTypeId;
    }

    public Date getDevelopmentDate() {
        return developmentDate;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ClaimValuePk)
            return equals((ClaimValuePk) o);
        return false;
    }
    
    private boolean equals(ClaimValuePk o) {
        return claimType == o.claimType &&
               dataTypeId == o.dataTypeId &&
               accidentDate.equals(o.accidentDate) &&
               developmentDate.equals(o.developmentDate);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + dataTypeId;
        hash = 17 * hash + (int) claimType;
        hash = 17 * hash + accidentDate.hashCode();
        return 17 * hash + developmentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("ClaimValuePk [%d; %d; %tF; %tF]",
              dataTypeId, claimType, accidentDate, developmentDate);
    }
}
