package org.jreserve.factor.core.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Version;
import org.hibernate.envers.NotAudited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.IdGenerator;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class LinkRatioSelection implements PersistentObject, Comparable<LinkRatioSelection> {
    
    @Id
    @Column(name="ID", length=AbstractPersistentObject.ID_LENGTH)
    private String id = IdGenerator.getId();
    
    @NotAudited
    @Version
    @Column(name="VERSION", nullable=false)
    private Long version;
    
    @NotAudited
    @Column(name="DEVELOPMENT", nullable=false)
    private int development;

    @Override
    public String getId() {
        return id;
    }
    
    protected void setId(String id) {
        this.id = id;
    }

    @Override
    public Long getVersion() {
        return version;
    }
    
    protected void setVersion(Long version) {
        this.version = version;
    }
    
    public int getDevelopment() {
        return development;
    }
    
    @Override
    public int compareTo(LinkRatioSelection o) {
        if(o == null) 
            return -1;
        return development - o.development;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof LinkRatioSelection)
            return id.equals(((LinkRatioSelection)o).id);
        return false;
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        String msg = "LinkRatioSelection [development = %d]";
        return String.format(msg, development);
    }
    
    public abstract double getLinkRatio(double[][] cummulatedSource, double[][] factors);
    
    
}
