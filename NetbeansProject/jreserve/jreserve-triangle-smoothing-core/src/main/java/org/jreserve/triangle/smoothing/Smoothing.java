package org.jreserve.triangle.smoothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.rutil.RCode;
import org.jreserve.rutil.RFunction;
import org.jreserve.rutil.RUtil;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.TriangularDataModification;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.entities.TriangleModification;

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
public abstract class Smoothing extends TriangleModification implements PersistentObject {

    public final static String LAYER_ID = "smoothing-layer";
    private final static int NAME_LENGTH = 64;
    
    @Column(name="SMOOTHING_NAME", length=NAME_LENGTH, nullable=false)
    private String name;
    
    @OneToMany(mappedBy="smoothing", orphanRemoval=true, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private Set<SmoothingCell> cells = new TreeSet<SmoothingCell>();
    
    protected Smoothing() {
    }
    
    protected Smoothing(int order, String name) {
        super(order);
        this.name = name;
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
    public String toString() {
        return String.format("Smoothing [%s; %s]", name, super.getId());
    }
}
