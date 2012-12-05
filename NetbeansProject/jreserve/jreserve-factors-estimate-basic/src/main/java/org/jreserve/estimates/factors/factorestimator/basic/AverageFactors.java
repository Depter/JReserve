package org.jreserve.estimates.factors.factorestimator.basic;

import org.jreserve.estimates.factors.factorestimator.AbstractFactorEstimator;
import org.jreserve.estimates.factors.factorestimator.FactorEstimator;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FactorEstimator.Registration(
    position=100,
    displayName="#LBL.AverageFactors.Name"
)
@Messages({
    "LBL.AverageFactors.Name=Average"
})
public class AverageFactors extends AbstractFactorEstimator {

    @Override
    protected double getFactorForColumn(int column, double[][] input, double[][] factors) {
        double result = 0d;
        int count = 0;
        
        for(double[] row : factors) {
            if(rowHasFactor(row, column)) {
                result += row[column];
                count++;
            }
        }
        
        return count==0? Double.NaN : result / (double)count;
    }

    private boolean rowHasFactor(double[] row, int column) {
        return row != null &&
               column < row.length &&
               !Double.isNaN(row[column]);
    }
}
