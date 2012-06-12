package org.jreserve.data.base;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name = "TRIANGLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Triangle.findAll", query = "SELECT t FROM Triangle t"),
    @NamedQuery(name = "Triangle.findById", query = "SELECT t FROM Triangle t WHERE t.id = :id")
})
public class Triangle implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name="org.jreserve.data.base.Triangle", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.TRIANGLE.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.base.Triangle")
    private long id;
    
    @Column(name = "NAME", nullable = false)
    @Size(min=1, max=64, message="invalid.name")
    private String name;
    
    @Column(name = "START_YEAR", nullable = false)
    private short startYear;
    
    @Column(name = "START_MONTH", nullable = false)
    @Max(value=11)
    @Min(value=0)
    private short startMonth;
    
    @Column(name = "MONTH_IN_DEVELOPMENT", nullable = false)
    @Min(value=1)
    private short monthInDevelopment;
    
    @Column(name = "MONTH_IN_ACCIDDENT", nullable = false)
    @Min(value=1)
    private short monthInAcciddent;
    
    @Column(name = "END_YEAR", nullable = false)
    private short endYear;
    
    @Column(name = "END_MONTH", nullable = false)
    @Max(value=11)
    @Min(value=0)
    private short endMonth;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "triangle", fetch = FetchType.LAZY)
    private List<TriangleCell> cells;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "triangle", fetch = FetchType.LAZY)
    private List<TriangleComment> comments;
    
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

    public Triangle() {
    }

    public Triangle(String name, ClaimType claimType, DataType dataType) {
        setName(name);
        setClaimType(claimType);
        setDataType(dataType);
        initContainers();
    }
    
    private void initContainers() {
        cells = new ArrayList<>();
        comments = new ArrayList<>();
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Lob getLob() {
        return lob;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        if(claimType == null) 
            throw new NullPointerException("ClaimType is null!");
        this.claimType = claimType;
        this.lob = claimType.getLob();
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
        ValidationUtilities.checkCalendarMonth(endMonth);
        this.startMonth = startMonth;
    }

    public short getMonthInDevelopment() {
        return monthInDevelopment;
    }

    public void setMonthInDevelopment(short monthInDevelopment) {
        if(monthInDevelopment < 1)
            throw new IllegalArgumentException("There must be at least 1 mont in a development period, not "+monthInDevelopment+"!");
        this.monthInDevelopment = monthInDevelopment;
    }

    public short getMonthInAcciddent() {
        return monthInAcciddent;
    }

    public void setMonthInAcciddent(short monthInAcciddent) {
        if(monthInAcciddent < 1)
            throw new IllegalArgumentException("There must be at least 1 mont in an accident period, not "+monthInAcciddent+"!");
        this.monthInAcciddent = monthInAcciddent;
    }
    
    public void setEnd(short endYear, short endMonth) {
        setEndYear(endYear);
        setEndMonth(endMonth);
    }

    public short getEndYear() {
        return endYear;
    }

    public void setEndYear(short endYear) {
        this.endYear = endYear;
    }

    public short getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(short endMonth) {
        ValidationUtilities.checkCalendarMonth(endMonth);
        this.endMonth = endMonth;
    }

    public List<TriangleCell> getCells() {
        return cells;
    }

    public void addCell(TriangleCell cell) {
        if(cell == null)
            throw new NullPointerException("Cell is null!");
        if(cells.contains(cell))
            return;
        cell.setTriangle(this);
        cells.add(cell);
    }
    
    public boolean removeCell(TriangleCell cell) {
        if(!cells.remove(cell))
            return false;
        cell.setTriangle(null);
        return true;
    }

    public List<TriangleComment> getComments() {
        return comments;
    }

    public void addComment(TriangleComment comment) {
        if(comment == null)
            throw new NullPointerException("Comment is null!");
        if(comments.contains(comment))
            return;
        comment.setTriangle(this);
        comments.add(comment);
    }
    
    public boolean removeComment(TriangleComment comment) {
        if(!comments.remove(comment))
            return false;
        comment.setTriangle(null);
        return true;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Triangle))
            return false;
        Triangle other = (Triangle) object;
        return id == other.id;
    }

    @Override
    public String toString() {
        String projectName = project==null? null : project.getName();
        return String.format("Triangle [%s; %s]", name, projectName);
    }
}
