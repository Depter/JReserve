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
@Table(name = "PROJECT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p"),
    @NamedQuery(name = "Project.findById", query = "SELECT p FROM Project p WHERE p.id = :id")
})
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name="org.jreserve.data.base.Project", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.PROJECT.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.base.Project")
    private long id;
    
    @Column(name = "NAME", nullable = false)
    @Size(min=1, max=64, message="invalid.name")
    private String name;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @JoinColumn(name = "LOB_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Lob lob;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project", fetch = FetchType.LAZY)
    private List<Vector> vectors;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project", fetch = FetchType.LAZY)
    private List<Triangle> triangles;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project", fetch = FetchType.LAZY)
    private List<ChangeLog> changeLogs;

    public Project() {
    }

    public Project(String name, Lob lob) {
        ValidationUtilities.checkStringLength64(name);
        this.name = name;
        if(lob == null) throw new NullPointerException("LoB is null!");
        this.lob = lob;
        initContainers();
    }
    
    private void initContainers() {
        vectors = new ArrayList<>();
        triangles = new ArrayList<>();
        changeLogs = new ArrayList<>();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Lob getLob() {
        return lob;
    }

    public void setLob(Lob lob) {
        if(lob == null) 
            throw new NullPointerException("LoB is null!");
        this.lob = lob;
    }

    public List<Vector> getVectors() {
        return vectors;
    }

    public void addVector(Vector vector) {
        if(vector == null)
            throw new NullPointerException("Vector is null!");
        if(vectors.contains(vector))
            return;
        vector.setProject(this);
        vectors.add(vector);
    }
    
    public boolean removeVector(Vector vector) {
        if(!vectors.remove(vector)) 
            return false;
        vector.setProject(null);
        return true;
    }
    
    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void addTriangle(Triangle triangle) {
        if(triangle == null)
            throw new NullPointerException("Triangle is null!");
        if(triangles.contains(triangle))
            return;
        triangle.setProject(this);
        triangles.add(triangle);
    }
    
    public boolean removeTriangle(Triangle triangle) {
        if(!triangles.remove(triangle)) 
            return false;
        triangle.setProject(null);
        return true;
    }

    public List<ChangeLog> getChangeLogs() {
        return changeLogs;
    }
    
    public void addChangeLog(ChangeLog log) {
        if(log == null)
            throw new NullPointerException("ChangeLog is null!");
        if(changeLogs.contains(log))
            return;
        log.setProject(this);
        changeLogs.add(log);
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Project))
            return false;
        Project other = (Project) object;
        return id == other.id;
    }

    @Override
    public String toString() {
        return String.format("Project [%s; %d]", name, lob.getId());
    }
}
