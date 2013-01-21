package org.jreserve.triangle.smoothing.geometric.entities;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.rutil.RFunction;
import org.jreserve.triangle.smoothing.Smoothing;
import org.jreserve.triangle.smoothing.SmoothingCell;
import org.jreserve.triangle.smoothing.geometric.RGeometricSmoothing;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - cells",
    "MSG.GeometricSmoothing.AuditText=Geometric smoothing {0}"
})
@EntityRegistration
@Audited
@Entity
@Table(name="GEOMETRIC_SMOOTHING", schema="JRESERVE")
public class GeometricSmoothing extends Smoothing {

    public GeometricSmoothing() {
    }
    
    public GeometricSmoothing(int order, String name) {
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
        return RGeometricSmoothing.getSmoothing(triangle, x, y, used);
    }

    @Override
    public RFunction getRFunction() {
        return new RGeometricSmoothing();
    }

    @Override
    public String createAuditRepresentation() {
        String cells = super.getCellsAuditRepresentation();
        return Bundle.MSG_GeometricSmoothing_AuditText(cells);
    }
}
