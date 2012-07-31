package org.jreserve.project.entities.project;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCorrectionPk implements Serializable {

    private long triangle;
    private Date accidentDate;
    private Date developmentDate;
    
    protected TriangleCorrectionPk() {
    }

    public long getTriangle() {
        return triangle;
    }

    public void setTriangle(long triangle) {
        this.triangle = triangle;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(Date accidentDate) {
        this.accidentDate = accidentDate;
    }

    public Date getDevelopmentDate() {
        return developmentDate;
    }

    public void setDevelopmentDate(Date developmentDate) {
        this.developmentDate = developmentDate;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleCorrectionPk)
            return equals((TriangleCorrectionPk) o);
        return false;
    }
    
    private boolean equals(TriangleCorrectionPk o) {
        return triangle == o.triangle &&
               accidentDate.equals(o.accidentDate) &&
               developmentDate.equals(o.developmentDate);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + (int) triangle;
        hash = 17 * hash + accidentDate.hashCode();
        return 17 * hash + developmentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("VectorCorrectionPk [%d; %tF / %tF]", 
                triangle, accidentDate, developmentDate);
    }
}