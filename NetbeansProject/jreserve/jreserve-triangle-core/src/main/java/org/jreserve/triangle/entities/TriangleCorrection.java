package org.jreserve.triangle.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Table(schema="JRESERVE", name="TRIANGLE_CORRECTION")
public class TriangleCorrection extends AbstractPersistentObject implements Serializable {
    private final static long serialVersionUID = 1L;
    
    @Column(name="ACCIDENT_PERIOD", nullable=false)
    private int accident;
    
    @Column(name="DEVELOPMENT_PERIOD", nullable=false)
    private int development;
    
    @Column(name="CORRECTION", nullable=false)
    private double correction;
    
    @Column(name="OWNER_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF, nullable=false)
    private String ownerId;
    
    protected TriangleCorrection() {
    }
    
    public TriangleCorrection(PersistentObject owner, int accident, int development) {
        this.ownerId = owner.getId();
        this.accident = accident;
        this.development = development;
    }

    public int getAccidentPeriod() {
        return accident;
    }
    
    public int getDevelopmentPeriod() {
        return development;
    }

    public double getCorrection() {
        return correction;
    }
    
    public void setCorrection(double correction) {
        this.correction = correction;
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleCorrection)
            return equals((TriangleCorrection) o);
        return false;
    }
    
    private boolean equals(TriangleCorrection o) {
        return ownerId.equalsIgnoreCase(o.ownerId) &&
               accident == o.accident &&
               development == o.development;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + ownerId.hashCode();
        hash = 17 * hash + accident;
        return 17 * hash + development;
    }
    
    @Override
    public String toString() {
        return String.format("[%d / %d]: %f", 
            accident, development, correction);
    }
}
