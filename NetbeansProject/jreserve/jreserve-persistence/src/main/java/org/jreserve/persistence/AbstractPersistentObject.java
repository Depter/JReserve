package org.jreserve.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractPersistentObject implements PersistentObject, Serializable {

    public final static int ID_LENGTH = 36;
    public final static String COLUMN_DEF = "varchar (36) not null";
    
    @Id
    @Column(name="ID", length=ID_LENGTH)
    private String id = IdGenerator.getId();
    
    @Version
    @Column(name="VERSION", nullable=false)
    private Long version;
    
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

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o==null || id==null || !(o instanceof PersistentObject))
            return false;
        return id.equals(((PersistentObject)o).getId());
    }
    
    @Override
    public int hashCode() {
        if(id == null)
            return super.hashCode();
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("%s [id=%s]", getClass().getName(), id);
    }
}
