package org.jreserve.triangle.value;

import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCoordiante implements Comparable<TriangleCoordiante> {
    
    private final int accident;
    private final int development;
    
    public TriangleCoordiante(int accident, int development) {
        this.accident = accident;
        this.development = development;
    }
    
    public int getAccident() {
        return accident;
    }
    
    public int getDevelopment() {
        return development;
    }
    
    public double getValue(TriangularData data) {
        return data.getValue(accident, development);
    }
    
    public double getValue(double[][] data) {
        if(data.length < accident)
            if(data[accident].length < development)
                return data[accident][development];
        return Double.NaN;
    }
    
    @Override
    public int compareTo(TriangleCoordiante coordinate) {
        if(coordinate == null)
            return -1;
        int dif = accident - coordinate.accident;
        if(dif != 0)
            return dif;
        return development - coordinate.development;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleCoordiante)
            return compareTo((TriangleCoordiante) o) == 0;
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