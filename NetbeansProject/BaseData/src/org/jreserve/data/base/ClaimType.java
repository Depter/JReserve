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
@Table(name = "CLAIM_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClaimType.findAll", query = "SELECT c FROM ClaimType c"),
    @NamedQuery(name = "ClaimType.findById", query = "SELECT c FROM ClaimType c WHERE c.id = :id"),
})
public class ClaimType implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name="org.jreserve.data.base.ClaimType", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.CLAIM_TYPE.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.base.ClaimType")
    private long id;
    
    @Column(name = "NAME", nullable = false)
    @Size(min=1, max=64, message="invalid.name")
    private String name;
    
    @Column(name = "LONG_NAME", nullable = false)
    @Size(min=1, max=255)
    private String longName;
    
    @JoinColumn(name = "LOB_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Lob lob;

    public ClaimType() {
    }

    public ClaimType(String name, String longName) {
        ValidationUtilities.checkStringLength64(name);
        this.name = name;
        ValidationUtilities.checkStringLength255(longName);
        this.longName = longName;
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

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        ValidationUtilities.checkStringLength64(longName);
        this.longName = longName;
    }

    public Lob getLob() {
        return lob;
    }

    public void setLob(Lob lob) {
        this.lob = lob;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ClaimType))
            return false;
        ClaimType other = (ClaimType) object;
        return id == other.id;
    }

    @Override
    public String toString() {
        return String.format("ClaimType [%s]", name);
    }
}
