package org.jreserve.data.base;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Embeddable
public class VectorCoordinate implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Basic(optional = false)
    @Column(name = "ACCIDENT_YEAR")
    private short accidentYear;
    
    @Basic(optional = false)
    @Column(name = "ACCIDENT_MONTH")
    @Min(value=0)
    @Max(value=11)
    private short accidentMonth;

    public VectorCoordinate() {
    }
    
    public VectorCoordinate(short accidentYear, short accidentMonth) {
        this.accidentYear = accidentYear;
        checkMonth(accidentMonth);
        this.accidentMonth = accidentMonth;
    }
    
    protected static void checkMonth(short month) {
        if(month >- 0 && month <= 11)
            return;
        String msg = String.format("Month %d is not within [1,11]", month);
        throw new IllegalArgumentException(msg);
    }

    public short getAccidentYear() {
        return accidentYear;
    }

    public void setAccidentYear(short accidentYear) {
        this.accidentYear = accidentYear;
    }

    public short getAccidentMonth() {
        return accidentMonth;
    }

    public void setAccidentMonth(short accidentMonth) {
        checkMonth(accidentMonth);
        this.accidentMonth = accidentMonth;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof VectorCoordinate))
            return false;
        VectorCoordinate vc = (VectorCoordinate) o;
        return accidentYear == vc.accidentYear &&
               accidentMonth == vc.accidentMonth;
    }
    
    @Override
    public int hashCode() {
        return 17 * (31 + accidentYear) + accidentMonth;
    }
    
    @Override
    public String toString() {
        return String.format("%d-%02d", accidentYear, accidentMonth);
    }
}
