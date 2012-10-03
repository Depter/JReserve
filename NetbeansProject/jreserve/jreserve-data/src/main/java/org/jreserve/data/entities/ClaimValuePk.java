package org.jreserve.data.entities;

import org.jreserve.data.ProjectDataType;
import java.io.Serializable;
import java.util.Date;
import org.jreserve.data.Data;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimValuePk implements Serializable {
    private final static long serialVersionUID = 1L;
    
    private long project;
    private long dataType;
    
    private Date accidentDate;
    private Date developmentDate;
    
    public ClaimValuePk() {
    }
    
    public ClaimValuePk(ProjectDataType pdt, Date accident, Date development) {
        this.project = pdt.getProject().getId();
        this.dataType = pdt.getId();
        this.accidentDate = accident;
        this.developmentDate = development;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public long getProject() {
        return project;
    }

    public long getProjectDataType() {
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
        return project == o.project &&
               dataType == o.dataType &&
               accidentDate.equals(o.accidentDate) &&
               developmentDate.equals(o.developmentDate);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + (int) dataType;
        hash = 17 * hash + (int) project;
        hash = 17 * hash + accidentDate.hashCode();
        return 17 * hash + developmentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("ClaimValuePk [%d; %d; %tF; %tF]",
              dataType, project, accidentDate, developmentDate);
    }
}
