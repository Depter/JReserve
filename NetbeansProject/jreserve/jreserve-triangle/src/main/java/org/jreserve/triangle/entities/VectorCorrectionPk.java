package org.jreserve.triangle.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorCorrectionPk implements Serializable {

    private long vector;
    private Date accidentDate;
    
    protected VectorCorrectionPk() {
    }

    public long getVector() {
        return vector;
    }

    public void setVector(long vectorId) {
        this.vector = vectorId;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(Date accidentDate) {
        this.accidentDate = accidentDate;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof VectorCorrectionPk)
            return equals((VectorCorrectionPk) o);
        return false;
    }
    
    private boolean equals(VectorCorrectionPk o) {
        return vector == o.vector &&
               accidentDate.equals(o.accidentDate);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + (int) vector;
        return 17 * hash + accidentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("VectorCorrectionPk [%d; %tF]", 
                vector, accidentDate);
    }
}