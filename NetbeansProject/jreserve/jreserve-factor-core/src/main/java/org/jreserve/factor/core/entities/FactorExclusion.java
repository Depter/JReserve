package org.jreserve.factor.core.entities;

import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.jreserve.factor.core.exclusion.ExclusionModification;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.TriangularDataModification;
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
    "MSG.FactorExclusion.AuditMessage=Exclusion [{0}; {1}]"
})
@EntityRegistration
@Audited
@Entity
@Table(name="FACTOR_EXCLUSION", schema="JRESERVE")
@Inheritance(strategy=InheritanceType.JOINED)
public class FactorExclusion extends TriangleModification implements TriangleCell.Provider {
    
    public final static String LAYER_ID = "FACTOR_EXCLUSION";
    
    @Embedded
    private TriangleCell cell;
    
    protected FactorExclusion() {
    }
    
    public FactorExclusion(int order, int accident, int development) {
        this(order, new TriangleCell(accident, development));
    }
    
    public FactorExclusion(int order, TriangleCell cell) {
        super(order);
        this.cell = cell;
    }
    
    @Override
    public TriangleCell getTriangleCell() {
        return cell;
    }

    @Override
    public TriangularDataModification createModification(TriangularData source) {
        return new ExclusionModification(source, cell);
    }

    @Override
    public String createAuditRepresentation() {
        int accident = cell.getAccident() + 1;
        int development = cell.getDevelopment() + 1;
        return Bundle.MSG_FactorExclusion_AuditMessage(accident, development);
    }
}
