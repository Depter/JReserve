package org.jreserve.data.base;

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
public class TriangleCoordinate extends VectorCoordinate {
    
    
    @Basic(optional = false)
    @Column(name = "DEVELOPMENT_YEAR")
    private short developmentYear;
    
    @Basic(optional = false)
    @Column(name = "DEVELOPMENT_MONTH")
    @Min(value=0)
    @Max(value=11)
    private short developmentMonth;

    public TriangleCoordinate() {
    }

    public TriangleCoordinate(short accidentYear, short accidentMonth, short developmentYear, short developmentMonth) {
        super(accidentYear, accidentMonth);
        this.developmentYear = developmentYear;
        VectorCoordinate.checkMonth(developmentMonth);
        this.developmentMonth = developmentMonth;
    }

    public short getDevelopmentMonth() {
        return developmentMonth;
    }

    public void setDevelopmentMonth(short developmentMonth) {
        VectorCoordinate.checkMonth(developmentMonth);
        this.developmentMonth = developmentMonth;
    }

    public short getDevelopmentYear() {
        return developmentYear;
    }

    public void setDevelopmentYear(short developmentYear) {
        this.developmentYear = developmentYear;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof TriangleCoordinate))
            return false;
        TriangleCoordinate tc = (TriangleCoordinate) o;
        return super.equals(tc) &&
               developmentYear == tc.developmentYear &&
               developmentMonth == tc.developmentMonth;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 * super.hashCode();
        hash = 17 * hash + developmentYear;
        return 17 * hash + developmentMonth;
    }
    
    @Override
    public String toString() {
        return String.format("%s, %d-%02d", super.toString(), developmentYear, developmentMonth);
    }
}
