package org.jreserve.triangle.smoothing.arithmetic.entities;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.rutil.RFunction;
import org.jreserve.triangle.smoothing.Smoothing;
import org.jreserve.triangle.smoothing.SmoothingCell;
import org.jreserve.triangle.smoothing.arithmetic.RArithmeticSmoothing;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "# {0} - cells",
    "MSG.ArithmeticSmoothing.AuditText=Arithmetic smoothing {0}"
})
@EntityRegistration
@Audited
@Entity
@Table(name="ARITHMETIC_SMOOTHING", schema="JRESERVE")
public class ArithmeticSmoothing extends Smoothing {

    public ArithmeticSmoothing() {
    }
    
    public ArithmeticSmoothing(int order, String name) {
        super(order, name);
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

    @Override
    public String createAuditRepresentation() {
        String cells = super.getCellsAuditRepresentation();
        return Bundle.MSG_ArithmeticSmoothing_AuditText(cells);
    }
}
