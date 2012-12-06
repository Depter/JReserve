package org.jreserve.triangle.smoothing;

import javax.persistence.*;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Table(name="SMOOTHING_CELL", schema="JRESERVE")
public class SmoothingCell extends AbstractPersistentObject implements Comparable<SmoothingCell> {
    
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="SMOOTHING_ID", referencedColumnName="ID", nullable=false)
    private Smoothing smoothing;
    
    @Column(name="ACCIDENT_PERIOD", nullable=false)
    private int accident;
    
    @Column(name="DEVELOPMENT_PERIOD", nullable=false)
    private int development;
    
    @Column(name="APPLIED", nullable=false)
    private boolean applied;
    
    protected SmoothingCell() {
    }
    
    public SmoothingCell(Smoothing smoothing, int accident, int development, boolean applied) {
        this.smoothing = smoothing;
        this.accident = accident;
        this.development = development;
        this.applied = applied;
    }

    public int getAccidentPeriod() {
        return accident;
    }
    
    public int getDevelopmentPeriod() {
        return development;
    }

    public boolean isApplied() {
        return applied;
    }
    
    public Smoothing getSmoothing() {
        return smoothing;
    }

    @Override
    public int compareTo(SmoothingCell o) {
        if(o == null) return -1;
        int dif = accident - o.accident;
        if(dif != 0) return dif;
        return development - o.development;
    }
}

