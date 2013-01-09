package org.jreserve.triangle.smoothing.geometric;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.rutil.RFunction;
import org.jreserve.triangle.smoothing.Smoothing;
import org.jreserve.triangle.smoothing.SmoothingCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="GEOMETRIC_SMOOTHING", schema="JRESERVE")
public class GeometricSmoothing extends Smoothing {

    protected GeometricSmoothing() {
    }
    
    public GeometricSmoothing(PersistentObject owner, int order, String name) {
        super(owner, order, name);
    }

    @Override
    public void addCell(SmoothingCell cell) {
        super.addCell(cell);
    }
    
    @Override
    public double[] smooth(double[] input) {
        int size = input.length;
        double mean = 1d;
        
        for(int i=0; i<size; i++)
            mean *= input[i];
        if(mean < 0d)
            throw new IllegalArgumentException("Input contained odd number of negative numbers!");
        mean = Math.pow(mean, 1d/((double) size));
        
        double[] result = new double[size];
        Arrays.fill(result, mean);
        return result;
    }

    @Override
    public String getRSmoothing(String triangle, String x, String y, String used) {
        return RGeometricFunction.getSmoothing(triangle, x, y, used);
    }

    @Override
    public RFunction getRFunction() {
        return new RGeometricFunction();
    }
}
