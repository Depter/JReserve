package org.jreserve.data.base;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Embeddable
public class VectorCellPK extends VectorCoordinate implements Serializable {
    @Basic(optional = false)
    @Column(name = "VECTOR_ID")
    private long vectorId;

    public VectorCellPK() {
    }

    public VectorCellPK(long vectorId, short accidentYear, short accidentMonth) {
        super(accidentYear, accidentMonth);
        this.vectorId = vectorId;
    }

    public long getVectorId() {
        return vectorId;
    }

    public void setVectorId(long vectorId) {
        this.vectorId = vectorId;
    }

    @Override
    public int hashCode() {
        return 17 * (31+super.hashCode()) + (int)vectorId;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VectorCellPK)) {
            return false;
        }
        VectorCellPK other = (VectorCellPK) object;
        return vectorId==other.vectorId &&
               super.equals(other);
    }

    @Override
    public String toString() {
        return String.format("VectorCellPK [%d; %s]", vectorId, super.toString());
    }

}
