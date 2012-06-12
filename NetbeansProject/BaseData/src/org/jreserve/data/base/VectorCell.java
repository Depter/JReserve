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
@Table(name = "VECTOR_CELL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VectorCell.findAll", query = "SELECT v FROM VectorCell v"),
    @NamedQuery(name = "VectorCell.findByVectorId", query = "SELECT v FROM VectorCell v WHERE v.vectorCellPK.vectorId = :vectorId")
})
public class VectorCell implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected VectorCellPK vectorCellPK;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ORIGINAL_VALUE")
    private Double originalValue;
    
    @Column(name = "VALUE")
    private Double value;
    
    @JoinColumn(name = "VECTOR_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Vector vector;

    public VectorCell() {
        this.vectorCellPK = new VectorCellPK();
    }

    public VectorCell(Vector vector, short startYear, short startMonth) {
        this.vectorCellPK = new VectorCellPK(vector.getId(), startYear, startMonth);
        this.vector = vector;
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

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }
    
    public short getAccidentYear() {
        return vectorCellPK.getAccidentYear();
    }
    
    public void setAccidentYear(short accidentYear) {
        vectorCellPK.setAccidentYear(accidentYear);
    }
    
    public short getAccidentMonth() {
        return vectorCellPK.getAccidentMonth();
    }
    
    public void setAccidentMonth(short accidentMonth) {
        vectorCellPK.setAccidentMonth(accidentMonth);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vectorCellPK != null ? vectorCellPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VectorCell)) {
            return false;
        }
        VectorCell other = (VectorCell) object;
        if ((this.vectorCellPK == null && other.vectorCellPK != null) || (this.vectorCellPK != null && !this.vectorCellPK.equals(other.vectorCellPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("VectorCell [%d-%02d] = %f/%f", getAccidentYear(), getAccidentMonth(), originalValue, value);
    }

}
