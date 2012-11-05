package org.jreserve.smoothing.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
@EntityRegistration
@Entity
@Table(name="SMOOTHING", schema="JRESERVE")
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class Smoothing extends AbstractPersistentObject {

    private final static int NAME_LENGTH = 64;
    
    @Column(name="OWNER_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF, nullable=false)
    private String ownerId;
    
    @Column(name="SMOOTHING_NAME", length=NAME_LENGTH, nullable=false)
    private String name;
    
    @OneToMany(mappedBy="smoothing", orphanRemoval=true, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<SmoothingCell> cells = new ArrayList<SmoothingCell>();
    
    protected Smoothing() {
    }
    
    protected Smoothing(PersistentObject owner, String name) {
        this.ownerId = owner.getId();
        this.name = name;
    }
    
    public String getOwner() {
        return ownerId;
    }
    
    public String getName() {
        return name;
    }
    
    public List<SmoothingCell> getCells() {
        return cells;
    }
    
    protected void addCell(SmoothingCell cell) {
        this.cells.add(cell);
        Collections.sort(cells);
    }
    
    public abstract double[] smooth(double[] input);
}
