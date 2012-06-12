package org.jreserve.data.base;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Entity
@Table(name = "VECTOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vector.findAll", query = "SELECT v FROM Vector v"),
    @NamedQuery(name = "Vector.findById", query = "SELECT v FROM Vector v WHERE v.id = :id")
})
public class Vector implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @TableGenerator(name="org.jreserve.data.base.Vector", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.VECTOR.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.base.Vector")
    private long id;

    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Project project;
    
    @JoinColumn(name = "LOB_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Lob lob;
    
    @JoinColumn(name = "CLAIM_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ClaimType claimType;
    
    @JoinColumn(name = "DATA_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DataType dataType;
    
    @Basic(optional = false)
    @Column(name = "NAME")
    @Size(min=1, max=64, message="invalid.name")
    private String name;
    
    @Basic(optional = false)
    @Column(name = "START_YEAR")
    private short startYear;
    
    @Basic(optional = false)
    @Column(name = "START_MONTH")
    @Min(value=0)
    @Max(value=11)
    private short startMonth;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vector", fetch = FetchType.LAZY)
    private List<VectorComment> comments;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vector", fetch = FetchType.LAZY)
    private List<VectorCell> cells;

    public Vector() {
    }

    public Vector(String name, ClaimType claimType, DataType dataType) {
        ValidationUtilities.checkStringLength64(name);
        this.name = name;
        if(claimType == null) throw new NullPointerException("ClaimType is null!");
        this.claimType = claimType;
        if(dataType == null) throw new NullPointerException("DataType is null!");
        this.dataType = dataType;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Lob getLob() {
        return lob;
    }

    public void setLob(Lob lob) {
        this.lob = lob;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        if(claimType == null) 
            throw new NullPointerException("ClaimType is null!");
        this.claimType = claimType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        if(dataType == null) 
            throw new NullPointerException("DataType is null!");
        this.dataType = dataType;
    }
    
    public void setStart(short startYear, short startMonth) {
        setStartYear(startYear);
        setStartMonth(startMonth);
    }
    
    public short getStartYear() {
        return startYear;
    }

    public void setStartYear(short startYear) {
        this.startYear = startYear;
    }

    public short getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(short startMonth) {
        ValidationUtilities.checkCalendarMonth(startMonth);
        this.startMonth = startMonth;
    }

    public List<VectorComment> getComments() {
        return comments;
    }

    public void addComment(VectorComment comment) {
        if(comments.contains(comment))
            return;
        comment.setVector(this);
        comments.add(comment);
    }
    
    public boolean removeComment(VectorComment comment) {
        if(!comments.contains(comment))
            return false;
        comment.setVector(null);
        return true;
    }

    public List<VectorCell> getCells() {
        return cells;
    }

    public void addCell(VectorCell cell) {
        if(cells.contains(cell))
            return;
        cell.setVector(this);
        cells.add(cell);
    }
    
    public boolean removeCell(VectorCell cell) {
        if(!cells.contains(cell))
            return false;
        cell.setVector(null);
        return true;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vector))
            return false;
        Vector other = (Vector) object;
        return id == other.id;
    }

    @Override
    public String toString() {
        String projectName = project==null? null : project.getName();
        return String.format("Vector [%s, %s]", name, projectName);
    }

}
