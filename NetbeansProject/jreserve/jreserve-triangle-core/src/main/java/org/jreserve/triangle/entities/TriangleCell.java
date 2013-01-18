package org.jreserve.triangle.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Embeddable
public class TriangleCell implements Comparable<TriangleCell>, Serializable {
    private final static long serialVersionUID = 1L;
    
    public static interface Provider {
        public TriangleCell getTriangleCell();
    }
    
    @Column(name="ACCIDENT_PERIOD", nullable=false)
    private int accident;
    
    @Column(name="DEVELOPMENT_PERIOD", nullable=false)
    private int development;
    
    protected TriangleCell() {
    }
    
    public TriangleCell(int accident, int development) {
        this.accident = accident;
        this.development = development;
    }
    
    public int getAccident() {
        return accident;
    }
    
    public int getDevelopment() {
        return development;
    }
    
    public boolean equals(int accident, int development) {
        return this.accident == accident &&
               this.development == development;
    }
    
    @Override
    public int compareTo(TriangleCell coordinate) {
        if(coordinate == null)
            return -1;
        int dif = accident - coordinate.accident;
        if(dif != 0)
            return dif;
        return development - coordinate.development;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleCell)
            return compareTo((TriangleCell) o) == 0;
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + accident;
        return 17 * hash + development;
    }
    
    @Override
    public String toString() {
        return String.format(
            "TriangleCoordinate [%d; %d]",
            accident, development);
    }

}
