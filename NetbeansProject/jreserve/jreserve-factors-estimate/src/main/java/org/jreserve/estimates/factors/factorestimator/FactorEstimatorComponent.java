package org.jreserve.estimates.factors.factorestimator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorEstimatorComponent implements Comparable<FactorEstimatorComponent>{
    
    private final FactorEstimator estimator;
    private final String name;
    private final int position;
    
    FactorEstimatorComponent(FactorEstimator estimator, String name, int position) {
        this.estimator = estimator;
        this.name = name;
        this.position = position;
    }

    public FactorEstimator getEstimator() {
        return estimator;
    }

    public double[] getFactors(double[][] input, double[][] factors) {
        return estimator.getFactors(input, factors);
    }
    
    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int compareTo(FactorEstimatorComponent o) {
        if(o == null)
            return -1;
        return position - o.position;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof FactorEstimatorComponent)
            return position == ((FactorEstimatorComponent)o).position;
        return false;
    }
    
    @Override
    public int hashCode() {
        return position;
    }
    
    @Override
    public String toString() {
        return String.format("FactorEstimateComponent [%s; %d]",
              name, position);
    }
}
