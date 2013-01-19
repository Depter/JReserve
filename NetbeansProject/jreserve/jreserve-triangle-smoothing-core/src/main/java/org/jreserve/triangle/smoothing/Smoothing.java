package org.jreserve.triangle.smoothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
import org.jreserve.triangle.TriangleModification;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.TriangularDataModification;
import org.jreserve.triangle.entities.TriangleCell;

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
    
    @Column(name="SMOOTHING_NAME", length=NAME_LENGTH, nullable=false)
    private String name;
    
    @OneToMany(mappedBy="smoothing", orphanRemoval=true, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private Set<SmoothingCell> cells = new TreeSet<SmoothingCell>();
    
    protected Smoothing() {
    }
    
    protected Smoothing(int order, String name) {
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
    
    public String getName() {
        return name;
    }
    
    public List<SmoothingCell> getCells() {
        return new ArrayList<SmoothingCell>(cells);
    }
    
    protected void addCell(SmoothingCell cell) {
        this.cells.add(cell);
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
        int i=0;
        for(SmoothingCell cell : cells) {
            TriangleCell coordinate = cell.getTriangleCell();
            x[i] = coordinate.getAccident() + 1;
            y[i] = coordinate.getDevelopment() + 1;
            used[i] = cell.isApplied();
            i++;
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
    public TriangularDataModification createModification(TriangularData source) {
        return new TriangleSmoothing(source, this);
    }

    @Override
    public int compareTo(TriangleModification o) {
        if(o == null)
            return -1;
        return order - o.getOrder();
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
