package org.jreserve.data.entities;

import java.io.Serializable;
import java.util.Date;
import org.jreserve.data.ProjectDataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimValuePk implements Serializable {
    private final static long serialVersionUID = 1L;
    
    private String claimType;
    private String dataType;
    
    private Date accidentDate;
    private Date developmentDate;
    
    public ClaimValuePk() {
    }
    
    public ClaimValuePk(ProjectDataType pdt, Date accident, Date development) {
        this.claimType = pdt.getClaimType().getId();
        this.dataType = pdt.getId();
        this.accidentDate = accident;
        this.developmentDate = development;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public String getProject() {
        return claimType;
    }

    public String getProjectDataType() {
        return dataType;
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
        return claimType.equals(o.claimType) &&
               dataType.equals(o.dataType) &&
               accidentDate.equals(o.accidentDate) &&
               developmentDate.equals(o.developmentDate);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + dataType.hashCode();
        hash = 17 * hash + claimType.hashCode();
        hash = 17 * hash + accidentDate.hashCode();
        return 17 * hash + developmentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("ClaimValuePk [%d; %d; %tF; %tF]",
              dataType, claimType, accidentDate, developmentDate);
    }
}
