package org.jreserve.project.entities.input;

import java.io.Serializable;
import javax.persistence.*;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(generateId=true)
@Entity
@Table(name="DATA_TYPE", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.project.entities.input.DataType",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    pkColumnValue="org.jreserve.project.entities.input.DataType"
)
public class DataType implements Serializable {
    private final static long serialVersionUID = 1L;
    private final static int NAME_LENGTH = 64;
    
    @Id 
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.project.entities.input.DataType")
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
            return name.equalsIgnoreCase(((DataType)o).name);
        return false;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("DataType [%d; %s]", id, name);
    }
}
