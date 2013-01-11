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
import org.jreserve.rutil.RCode;
import org.jreserve.rutil.RFunction;
import org.jreserve.rutil.RUtil;

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
public abstract class Smoothing implements PersistentObject {

    public final static String LAYER_ID = "smoothing-layer";
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
    
    protected Smoothing(PersistentObject owner, int order, String name) {
        this.ownerId = owner.getId();
        this.order = order;
        this.name = name;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    protected void setId(String id) {
        this.id = id;
    }
    
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
    
    public void appendSmoothing(String triangleName, RCode rCode) {
        rCode.addFunction(getRFunction());
        
        int size = cells.size();
        int[] x = new int[size];
        int[] y = new int[size];
        boolean[] used = new boolean[size];
        for(int i=0; i<size; i++) {
            SmoothingCell cell = cells.get(i);
            x[i] = cell.getAccidentPeriod() + 1;
            y[i] = cell.getDevelopmentPeriod() + 1;
            used[i] = cell.isApplied();
        }
        
        String r = String.format("%s <- %s%n", triangleName, getRSmoothingString(triangleName, x, y, used));
        rCode.addSource(r);
    }
    
    private String getRSmoothingString(String triangle, int[] x, int[] y, boolean[] applied) {
        return getRSmoothing(triangle, 
              RUtil.createVector(x), 
              RUtil.createVector(y), 
              RUtil.createVector(applied));
    }
    
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
