package org.jreserve.project.entities.project;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Type;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.project.entities.input.LoB;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(generateId=true)
@Entity
@Table(name="PROJECT", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.project.entities.project.Project",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    pkColumnValue="org.jreserve.project.entities.project.Project"
)
public class Project implements Serializable {
    private final static long serialVersionUID = 1L;

    private final static int NAME_LENGTH = 64;
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.project.entities.project.Project")
    @Column(name="ID")
    private long id;
    
    @ManyToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(name="LOB_ID", referencedColumnName="ID", nullable=false)
    private LoB lob;
    
    @Column(name="PROJECT_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @Column(name="PROJECT_DESCRIPTION")
    @Type(type="org.hibernate.type.TextType")
    private String description;
    
    protected Project() {
    }
    
    public Project(LoB lob, String name) {
        initLob(lob);
        initName(name);
    }
    
    private void initLob(LoB lob) {
        if(lob == null)
            throw new NullPointerException("LoB is null!");
        this.lob = lob;
    }
    
    private void initName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }
    
    public long getId() {
        return id;
    }
    
    public LoB getLoB() {
        return lob;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        initName(name);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Project)
            return equals((Project) o);
        return false;
    }
    
    private boolean equals(Project o) {
        return lob.equals(o.lob) &&
               name.equalsIgnoreCase(o.name);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + lob.hashCode();
        return 17 * hash + name.toLowerCase().hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Project [%s; %s]", name, lob.getName());
    }
}
