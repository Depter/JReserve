package org.jreserve.data.base;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Entity
@Table(name = "DATA_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataType.findAll", query = "SELECT d FROM DataType d"),
    @NamedQuery(name = "DataType.findById", query = "SELECT d FROM DataType d WHERE d.id = :id")
})
public class DataType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name="org.jreserve.data.base.DataType", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.DATA_TYPE.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.base.DataType")
    private long id;
    
    @Column(name = "NAME", nullable = false)
    @Size(min=1, max=64, message="invalid.name")
    private String name;
    
    @Column(name = "IS_TRIANGLE", nullable = false)
    private boolean isTriangle;
    
    public DataType() {
    }

    public DataType(String name, boolean isTriangle) {
        ValidationUtilities.checkStringLength64(name);
        this.name = name;
        this.isTriangle = isTriangle;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        ValidationUtilities.checkStringLength64(name);
        this.name = name;
    }

    public boolean isTriangle() {
        return isTriangle;
    }

    public void setIsTriangle(boolean isTriangle) {
        this.isTriangle = isTriangle;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DataType))
            return false;
        DataType other = (DataType) object;
        return id == other.id;
    }

    @Override
    public String toString() {
        return String.format("DataType [%s]", name);
    }

}
