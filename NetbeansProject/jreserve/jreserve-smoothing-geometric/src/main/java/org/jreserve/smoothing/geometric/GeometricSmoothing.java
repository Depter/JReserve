package org.jreserve.smoothing.geometric;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.core.SmoothingCell;

/**
 *
 * @author Peter Decsi
 */
@EntityRegistration
@Audited
@Entity
@Table(name="GEOMETRIC_SMOOTHING", schema="JRESERVE")
public class GeometricSmoothing extends Smoothing {

    protected GeometricSmoothing() {
    }
    
    protected GeometricSmoothing(PersistentObject owner, String name) {
        super(owner, name);
    }

    @Override
    protected void addCell(SmoothingCell cell) {
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

}
