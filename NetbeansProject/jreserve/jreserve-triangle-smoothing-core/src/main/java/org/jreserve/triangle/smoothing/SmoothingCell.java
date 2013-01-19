package org.jreserve.triangle.smoothing;

import javax.persistence.*;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.triangle.entities.TriangleCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Table(name="SMOOTHING_CELL", schema="JRESERVE")
public class SmoothingCell extends AbstractPersistentObject implements Comparable<SmoothingCell>, TriangleCell.Provider {
    
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="SMOOTHING_ID", referencedColumnName="ID", nullable=false)
    private Smoothing smoothing;
    
    @Embedded
    private TriangleCell cell;
    
    @Column(name="APPLIED", nullable=false)
    private boolean applied;
    
    protected SmoothingCell() {
    }
    
    public SmoothingCell(Smoothing smoothing, int accident, int development, boolean applied) {
        this(smoothing, new TriangleCell(accident, development), applied);
    }
    
    public SmoothingCell(Smoothing smoothing, TriangleCell cell, boolean applied) {
        this.smoothing = smoothing;
        this.cell = cell;
        this.applied = applied;
    }
    
    @Override
    public TriangleCell getTriangleCell() {
        return cell;
    }
    
    public boolean isSameCell(int accident, int development) {
        return cell.equals(accident, development);
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
        return cell.compareTo(o.cell);
    }
}

