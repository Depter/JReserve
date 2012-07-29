package org.jreserve.project.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(entityClass=DataType.class)
@Entity
@Table(name="LOB", schema="JRESERVE")
public class DataType implements Serializable {
    private final static long serialVersionUID = 1L;
    private final static int NAME_LENGTH = 64;
    
    @Id
    @Column(name="ID", nullable=false)
    private long id;
    
    @Column(name="LOB_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    protected DataType() {
    }
    
    public DataType(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof DataType)
            return id == ((DataType)o).id;
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int) id;
    }
    
    @Override
    public String toString() {
        return String.format("DataType [%d; %s]", id, name);
    }
}
