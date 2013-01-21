package org.jreserve.triangle.correction.entities;

import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.TriangularDataModification;
import org.jreserve.triangle.correction.TriangleCorrectionModification;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.entities.TriangleModification;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - accident",
    "# {1} - development",
    "# {2} - value",
    "MSG.TriangleCorrection.AuditMessage=Correction [{0}; {1}] = {2}"
})
@EntityRegistration
@Audited
@Entity
@Table(name="TRIANGLE_CORRECTION", schema="JRESERVE")
@Inheritance(strategy=InheritanceType.JOINED)
public class TriangleCorrection extends TriangleModification implements TriangleCell.Provider {
    
    public final static String LAYER_ID = "TRIANGLE_CORRECTION";

    @Column(name="CORRIGATED_VALUE", nullable=false)
    private double corrigatedValue;
    
    @Column(name="MODIFICATION_ORDER", nullable=false)
    private int order;
    
    @Embedded
    private TriangleCell cell;
    
    protected TriangleCorrection() {
    }
    
    public TriangleCorrection(int order, int accident, int development, double corrigatedValue) {
        this(order, new TriangleCell(accident, development), corrigatedValue);
    }
    
    public TriangleCorrection(int order, TriangleCell cell, double corrigatedValue) {
        this.order = order;
        this.cell = cell;
        this.corrigatedValue = corrigatedValue;
    }
    
    @Override
    public int getOrder() {
        return order;
    }
    
    @Override
    public TriangleCell getTriangleCell() {
        return cell;
    }
    
    public double getCorrigatedValue() {
        return corrigatedValue;
    }

    @Override
    public TriangularDataModification createModification(TriangularData source) {
        return new TriangleCorrectionModification(source, this);
    }

    @Override
    public String createAuditRepresentation() {
        int accident = cell.getAccident() + 1;
        int development = cell.getDevelopment() + 1;
        return Bundle.MSG_TriangleCorrection_AuditMessage(accident, development, corrigatedValue);
    }

    @Override
    public int compareTo(TriangleModification o) {
        if(o == null)
            return -1;
        return order - o.getOrder();
    }
}
