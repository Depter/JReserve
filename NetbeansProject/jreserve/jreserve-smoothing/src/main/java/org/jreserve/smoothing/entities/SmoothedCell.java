package org.jreserve.smoothing.entities;

import java.util.Date;
import javax.persistence.*;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;

/**
 *
 * @author Peter Decsi
 */
@EntityRegistration
@Entity
@Table(name="SMOOTHED_CELL", schema="JRESERVE")
public class SmoothedCell extends AbstractPersistentObject {

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="SMOOTHING_ID", referencedColumnName="ID", nullable=false)
    private Smoothing smoothing;
    
    @Column(name="ACCIDENT", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date accident;
    
    @Column(name="DEVELOPMENT", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date development;
    
    protected SmoothedCell() {
    }
    
    SmoothedCell(Smoothing smoothing, Date accident, Date development) {
        this.smoothing = smoothing;
        this.accident = accident;
        this.development = development;
    }

    public Date getAccident() {
        return accident;
    }

    public Date getDevelopment() {
        return development;
    }

    public Smoothing getSmoothing() {
        return smoothing;
    }
    
    @Override
    public String toString() {
        return String.format("SmoothCell [%tF/%tF]", accident, development);
    }
}
