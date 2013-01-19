package org.jreserve.triangle.smoothing.exponential;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.rutil.RFunction;
import org.jreserve.triangle.smoothing.Smoothing;
import org.jreserve.triangle.smoothing.SmoothingCell;
import org.jreserve.triangle.smoothing.exponential.RExponentialSmoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="EXPONENTIAL_SMOOTHING", schema="JRESERVE")
public class ExponentialSmoothing extends Smoothing {

    @Column(name="ALPHA", nullable=false)
    private double alpha;
    
    protected ExponentialSmoothing() {
    }
    
    public ExponentialSmoothing(int order, String name, double alpha) {
        super(order, name);
        setCheckedAlpha(alpha);
    }

    private void setCheckedAlpha(double alpha) {
        if(alpha < 0d || alpha > 1d)
            throw new IllegalArgumentException(String.format("Alpha mut be within [0, 1], but was %f!", alpha));
        this.alpha = alpha;
    }
    
    @Override
    public void addCell(SmoothingCell cell) {
        super.addCell(cell);
    }
    
    @Override
    public double[] smooth(double[] input) {
        int size = input.length;
        double[] output = new double[size];
        System.arraycopy(input, 0, output, 0, size);
        
        for(int i=1; i<size; i++)
            output[i] = alpha * output[i-1] + (1-alpha) * output[i];
        
        return output;
    }

    @Override
    public String getRSmoothing(String triangle, String x, String y, String used) {
        return RExponentialSmoothing.getSmoothing(triangle, x, y, used, alpha);
    }

    @Override
    public RFunction getRFunction() {
        return new RExponentialSmoothing();
    }
    
    public double getAlpha() {
        return alpha;
    }
    
    void setAlpha(double alpha) {
        setCheckedAlpha(alpha);
    }
}
