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
    displayName="#LBL.WeightedAverageFactors.Name"
)
@Messages({
    "LBL.WeightedAverageFactors.Name=Weighted average"
})
public class WeightedAverageFactors extends AbstractFactorEstimator {

    @Override
    protected double getFactorForColumn(int column, double[][] input, double[][] factors) {
        double result = 0d;
        double weights = 0d;
        int count = 0;
        
        for(int r=0, size=factors.length; r<size; r++) {
            double[] row = factors[r];
            double[] inputRow = input[r];
            
            if(rowHasFactor(inputRow, row, column)) {
                double weight = inputRow[column];
                result += row[column] * weight;
                weights += weight;
                count++;
            }
        }
        
        return count==0? Double.NaN : result / weights;
    }

    private boolean rowHasFactor(double[] inputRow, double[] row, int column) {
        return row != null && inputRow != null &&
               column < row.length && column < inputRow.length &&
               !Double.isNaN(row[column]) && !Double.isNaN(inputRow[column]);
    }
}
