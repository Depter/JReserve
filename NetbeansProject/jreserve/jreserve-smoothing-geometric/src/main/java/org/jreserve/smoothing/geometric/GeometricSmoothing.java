package org.jreserve.smoothing.geometric;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.smoothing.Smoothing;

/**
 *
 * @author Peter Decsi
 */
@EntityRegistration
@Entity
@Table(name="GEOMETRIC_SMOOTHING", schema="JRESERVE")
public class GeometricSmoothing extends Smoothing {

    protected GeometricSmoothing() {
    }
    
    protected GeometricSmoothing(PersistentObject owner, String name) {
        super(owner, name);
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

}
