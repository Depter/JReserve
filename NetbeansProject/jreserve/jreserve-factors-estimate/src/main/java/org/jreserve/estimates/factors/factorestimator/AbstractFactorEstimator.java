package org.jreserve.estimates.factors.factorestimator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractFactorEstimator implements FactorEstimator {

    @Override
    public double[] getFactors(double[][] input, double[][] factors) {
        int count = getColumnCount(factors);
        double[] result = new double[count];
        for(int i=0; i<count; i++)
            result[i] = getFactorForColumn(i, input, factors);
        return result;
    }
    
    protected int getColumnCount(double[][] values) {
        if(values == null) return 0;
        int count = 0;
        for(double[] row : values)
            if(row != null && count<row.length)
                count = row.length;
        return count;
    }
    
    protected abstract double getFactorForColumn(int column, double[][] input, double[][] factors);
}
