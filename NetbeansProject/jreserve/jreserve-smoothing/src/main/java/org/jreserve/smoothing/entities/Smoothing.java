package org.jreserve.smoothing.entities;

import org.jreserve.smoothing.SmoothMethod;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
public class Smoothing extends AbstractPersistentObject {
    private final static int NAME_LENGTH = 64;

    @Column(name="SMOOTH_TYPE", nullable=false, columnDefinition=AbstractPersistentObject.COLUMN_DEF)
    private String ownerId;
    
    @Column(name="SMOOTH_METHOD", nullable=false)
    private int smoothMethodId;
    
    @Column(name="SMOOTHING_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @OneToMany(mappedBy="smoothing", orphanRemoval=true)
    private List<SmoothedCell> cells = new ArrayList<SmoothedCell>();
    
    protected Smoothing() {
    }
    
    public Smoothing(PersistentObject owner, SmoothMethod method, String name) {
        initOwner(owner);
        initMethod(method);
        initName(name);
    }
    
    private void initOwner(PersistentObject owner) {
        if(owner == null)
            throw new NullPointerException("Owner was null!");
        this.ownerId = owner.getId();
    }
    
    private void initName(String name) {
        if(name == null)
            throw new NullPointerException("Name is null!");
        if(name.trim().length() == 0)
            throw new IllegalArgumentException("Name is empty string!");
        if(name.length() > NAME_LENGTH)
            throw new IllegalArgumentException("Name was langer then max length! "+name.length()+" > "+NAME_LENGTH);
        this.name = name;
    }
    
    private void initMethod(SmoothMethod method) {
        if(method == null)
            throw new NullPointerException("Method is null!");
        this.smoothMethodId = method.getId();
    }

    public String getName() {
        return name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public int getSmoothMethodId() {
        return smoothMethodId;
    }

    public List<SmoothedCell> getCells() {
        return new ArrayList<SmoothedCell>(cells);
    }
    
    public void addCell(Date accident, Date development) {
        checkDates(accident, development);
        if(!containsCell(accident, development))
            cells.add(new SmoothedCell(this, accident, development));
    }
    
    private void checkDates(Date accident, Date development) {
        if(accident == null)
            throw new NullPointerException("Accident is null!");
        if(development == null)
            throw new NullPointerException("Development is null!");
        if(development.before(accident))
            throw new IllegalArgumentException(String.format("Development before accident! %tF < %tF", development, accident));
    }
    
    public boolean containsCell(Date accident, Date development) {
        for(SmoothedCell cell : cells)
            if(cell.getAccident().equals(accident) && cell.getDevelopment().equals(development))
                return true;
        return false;
    }
}
