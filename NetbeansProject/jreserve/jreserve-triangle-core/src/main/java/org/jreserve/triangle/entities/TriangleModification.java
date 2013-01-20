package org.jreserve.triangle.entities;

import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.IdGenerator;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.TriangularDataModification;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="TRIANGLE_MODIFICATION", schema="JRESERVE")
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class TriangleModification implements PersistentObject, Comparable<TriangleModification> {
    
    public final static String MODIFICATION_PROPERTY = "TRIANGLE_MODIFICATION";
    
    @Id
    @Column(name="ID", length=AbstractPersistentObject.ID_LENGTH)
    private String id = IdGenerator.getId();
    
    @NotAudited
    @Version
    @Column(name="VERSION", nullable=false)
    private Long version;
    
    @NotAudited
    @Column(name="SMOOTH_ORDER", nullable=false)
    private int order;
    
    protected TriangleModification() {
    }
    
    protected TriangleModification(int order) {
        this.order = order;
    }

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

    public int getOrder() {
        return order;
    }

    @Override
    public int compareTo(TriangleModification o) {
        if(o == null)
            return -1;
        return order - o.getOrder();
    }
    
    public abstract TriangularDataModification createModification(TriangularData source);
}
