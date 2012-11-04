package org.jreserve.smoothing;

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
@Table(name="SMOOTHING_CELL", schema="JRESERVE")
public class SmoothingCell extends AbstractPersistentObject implements Comparable<SmoothingCell> {
    
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="SMOOTHING_ID", referencedColumnName="ID", nullable=false)
    private Smoothing smoothing;
    
    @Column(name="ACCIDENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date accident;
    
    @Column(name="DEVELOPMENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date development;
    
    protected SmoothingCell() {
    }
    
    public SmoothingCell(Smoothing smoothing, Date accident, Date development) {
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
    public int compareTo(SmoothingCell o) {
        if(o == null) return -1;
        int dif = accident.compareTo(o.accident);
        if(dif != 0) return dif;
        return development.compareTo(o.development);
    }
}
