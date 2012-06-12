package org.jreserve.data.base;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
        
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Entity
@Table(name = "TRIANGLE_CELL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TriangleCell.findAll", query = "SELECT t FROM TriangleCell t"),
    @NamedQuery(name = "TriangleCell.findByTriangleId", query = "SELECT t FROM TriangleCell t WHERE t.triangleCellPK.triangleId = :triangleId")
})
public class TriangleCell implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected TriangleCellPK triangleCellPK;
    
    @Column(name = "ORIGINAL_VALUE")
    private Double originalValue;
    
    @Column(name = "VALUE")
    private Double value;
    
    @JoinColumn(name = "TRIANGLE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Triangle triangle;

    public TriangleCell() {
        this.triangleCellPK = new TriangleCellPK();
    }

    public TriangleCell(Triangle triangle, short accidentYear, short accidentMonth, short developmentYear, short developmentMonth) {
        this.triangleCellPK = new TriangleCellPK(triangle.getId(), accidentYear, accidentMonth, developmentYear, developmentMonth);
        this.triangle = triangle;
    }

    public Double getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(Double originalValue) {
        this.originalValue = originalValue;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public void setTriangle(Triangle triangle) {
        this.triangle = triangle;
        long id = triangle==null? -1 : triangle.getId();
        triangleCellPK.setTriangleId(id);
    }

    public short getDevelopmentMonth() {
        return triangleCellPK.getDevelopmentMonth();
    }

    public void setDevelopmentMonth(short developmentMonth) {
        triangleCellPK.setDevelopmentMonth(developmentMonth);
    }

    public short getDevelopmentYear() {
        return triangleCellPK.getDevelopmentYear();
    }

    public void setDevelopmentYear(short developmentYear) {
        triangleCellPK.setDevelopmentMonth(developmentYear);
    }

    public short getAccidentMonth() {
        return triangleCellPK.getAccidentMonth();
    }

    public void setAccidentMonth(short accidentMonth) {
        triangleCellPK.setAccidentMonth(accidentMonth);
    }

    public short getAccidentYear() {
        return triangleCellPK.getAccidentYear();
    }

    public void setAccidentYear(short accidentYear) {
        triangleCellPK.setAccidentMonth(accidentYear);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (triangleCellPK != null ? triangleCellPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TriangleCell)) {
            return false;
        }
        TriangleCell other = (TriangleCell) object;
        if ((this.triangleCellPK == null && other.triangleCellPK != null) || (this.triangleCellPK != null && !this.triangleCellPK.equals(other.triangleCellPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String str = "TrinagleCell [%s]: %f/%f";
        return String.format(str, triangleCellPK, value, originalValue);
    }

}
