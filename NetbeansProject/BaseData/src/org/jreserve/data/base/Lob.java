package org.jreserve.data.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Entity
@Table(name = "LOB")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lob.findAll", query = "SELECT l FROM Lob l"),
    @NamedQuery(name = "Lob.findById", query = "SELECT l FROM Lob l WHERE l.id = :id")
})
public class Lob implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name="org.jreserve.data.base.Lob", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.LOB.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.base.Lob")
    private long id;
    
    @Column(name = "NAME", nullable = false)
    @Size(min=1, max=64, message="invalid.name")
    private String name;
    
    @Column(name = "LONG_NAME", nullable = false)
    @Size(min=1, max=255, message="invalid.longname")
    private String longName;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lob", fetch = FetchType.EAGER)
    private List<ClaimType> claimTypes = new ArrayList<>();

    public Lob() {
    }

    public Lob(String name, String longName) {
        ValidationUtilities.checkStringLength64(name);
        this.name = name;
        ValidationUtilities.checkStringLength255(name);
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
        ValidationUtilities.checkStringLength255(name);
        this.longName = longName;
    }

    public List<ClaimType> getClaimTypes() {
        return claimTypes;
    }
    
    public void addClaimType(ClaimType claimType) {
        if(claimType == null)
            throw new NullPointerException("ClaimType is null!");
        if(claimTypes.contains(claimType))
            return;
        claimType.setLob(this);
        claimTypes.add(claimType);
    }
    
    public boolean removeClaimType(ClaimType claimType) {
        if(!claimTypes.remove(claimType)) 
            return false;
        claimType.setLob(null);
        return true;
    }
    
    
    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Lob))
            return false;
        Lob other = (Lob) object;
        return id == other.id;
    }

    @Override
    public String toString() {
        return String.format("Lob [%s]", name);
    }

}
