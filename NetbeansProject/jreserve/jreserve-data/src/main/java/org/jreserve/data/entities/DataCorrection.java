package org.jreserve.data.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="DATA_CORRECTION", schema="JRESERVE")
public class DataCorrection extends AbstractData {

    @Column(name="CORRECTION", nullable=false)
    private double correction;
    
    protected DataCorrection() {
    }
    
    public DataCorrection(PersistentObject owner, Date accident, Date development, double correction) {
        super(owner, accident, development);
        this.correction = correction;
    }
    
    public double getCorrection() {
        return correction;
    }

    public void setCorrection(double correction) {
        this.correction = correction;
    }
    
    @Override
    public String toString() {
        return String.format("DataCorrection [%s, %f]",
                getDateString(), correction);
    }
}
