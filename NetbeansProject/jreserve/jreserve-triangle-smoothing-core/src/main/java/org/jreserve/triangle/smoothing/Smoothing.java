package org.jreserve.triangle.smoothing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.IdGenerator;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.rutil.RFunction;
import org.jreserve.triangle.TriangleModification;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="SMOOTHING", schema="JRESERVE")
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class Smoothing implements PersistentObject, TriangleModification {

    private final static int NAME_LENGTH = 64;
    
    @Id
    @Column(name="ID", length=AbstractPersistentObject.ID_LENGTH)
    private String id = IdGenerator.getId();
    
    @NotAudited
    @Column(name="SMOOTH_ORDER", nullable=false)
    private int order;
    
    @NotAudited
    @Version
    @Column(name="VERSION", nullable=false)
    private Long version;
    
    @Column(name="OWNER_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF, nullable=false)
    private String ownerId;
    
    @Column(name="SMOOTHING_NAME", length=NAME_LENGTH, nullable=false)
    private String name;
    
    @NotAudited
    @OneToMany(mappedBy="smoothing", orphanRemoval=true, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<SmoothingCell> cells = new ArrayList<SmoothingCell>();
    
    protected Smoothing() {
    }
    
    protected Smoothing(PersistentObject owner, String name) {
        this.ownerId = owner.getId();
        this.name = name;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    protected void setId(String id) {
        this.id = id;
    }
    
    public void setOrder(int order) {
        this.order = order;
    }
    
    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public Long getVersion() {
        return version;
    }
    
    protected void setVersion(Long version) {
        this.version = version;
    }
    
    @Override
    public String getOwnerId() {
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

    public abstract String getRSmoothing(String triangle, String x, String y, String used);
    
    public abstract RFunction getRFunction();
    
    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o==null || id==null || !(o instanceof Smoothing))
            return false;
        return id.equals(((Smoothing)o).getId());
    }
    
    @Override
    public int hashCode() {
        if(id == null)
            return super.hashCode();
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Smoothing [%s; %s]", name, id);
    }
}