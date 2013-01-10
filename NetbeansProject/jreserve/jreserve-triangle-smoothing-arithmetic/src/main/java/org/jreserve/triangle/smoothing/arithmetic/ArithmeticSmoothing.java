package org.jreserve.triangle.smoothing.arithmetic;

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
 */
@EntityRegistration
@Audited
@Entity
@Table(name="ARITHMETIC_SMOOTHING", schema="JRESERVE")
public class ArithmeticSmoothing extends Smoothing {

    protected ArithmeticSmoothing() {
    }
    
    public ArithmeticSmoothing(PersistentObject owner, int order, String name) {
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
            mean += input[i];
        mean = size==0? Double.NaN : mean /(double) size;
        
        double[] result = new double[size];
        Arrays.fill(result, mean);
        return result;
    }

    @Override
    public String getRSmoothing(String triangle, String x, String y, String used) {
        return RArithmeticSmoothing.getSmoothing(triangle, x, y, used);
    }

    @Override
    public RFunction getRFunction() {
        return new RArithmeticSmoothing();
    }
}
