package org.jreserve.triangle.correction.entities;

import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="TRIANGLE_CORRECTION", schema="JRESERVE")
@Inheritance(strategy=InheritanceType.JOINED)
public class TriangleCorrection extends AbstractPersistentObject {
    
    public final static String LAYER_ID = "TRIANGLE_CORRECTION";
    
    @Column(name="OWNER_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF, nullable=false)
    private String ownerId;
    
    @Column(name="ACCIDENT_PERIOD")
    private int accident;
    
    @Column(name="DEVELOPMENT_PERIOD")
    private int development;

    @Column(name="CORRIGATED_VALUE")
    private double corrigatedValue;
    
    @Column(name="MODIFICATION_ORDER", nullable=false)
    private int order;
    
    protected TriangleCorrection() {
    }
    
    public TriangleCorrection(String ownerId, int order, int accident, int development, int corrigatedValue) {
        this.ownerId = ownerId;
        this.order = order;
        this.accident = accident;
        this.development = development;
        this.corrigatedValue = corrigatedValue;
    }
    
    public int getOrder() {
        return order;
    }
    
    public int getAccident() {
        return accident;
    }
    
    public int getDevelopment() {
        return development;
    }
    
    public double getCorrigatedValue() {
        return corrigatedValue;
    }
}
