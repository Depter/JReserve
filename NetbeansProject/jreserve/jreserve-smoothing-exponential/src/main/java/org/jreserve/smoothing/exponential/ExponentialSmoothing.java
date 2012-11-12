package org.jreserve.smoothing.exponential;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.core.SmoothingCell;

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
    
    @NotAudited
    @Column(name="ALPHA", nullable=false)
    private double alpha;
    
    protected ExponentialSmoothing() {
    }
    
    protected ExponentialSmoothing(double alpha) {
        this.alpha = alpha;
    }
    
    protected ExponentialSmoothing(PersistentObject owner, String name, double alpha) {
        super(owner, name);
        this.alpha = alpha;
    }
    
    public double getAlpha() {
        return alpha;
    }
    
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    protected void addCell(SmoothingCell cell) {
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
    
}
