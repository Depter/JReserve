package org.jreserve.data.base;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Embeddable
public class TriangleCellPK extends TriangleCoordinate implements Serializable {

    @Column(name = "TRIANGLE_ID", nullable = false)
    private long triangleId;

    public TriangleCellPK() {
    }

    public TriangleCellPK(long triangleId, short accidentYear, short accidentMonth, short developmentYear, short developmentMonth) {
        super(accidentYear, accidentMonth, developmentYear, developmentMonth);
        this.triangleId = triangleId;
    }

    public long getTriangleId() {
        return triangleId;
    }

    public void setTriangleId(long triangleId) {
        this.triangleId = triangleId;
    }

    @Override
    public int hashCode() {
        return 17 * (31 + super.hashCode()) + (int)triangleId;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TriangleCellPK))
            return false;
        TriangleCellPK other = (TriangleCellPK) object;
        return super.equals(object) &&
               triangleId == other.triangleId;
    }

    @Override
    public String toString() {
        return String.format("TriangleCellPK [%s, %d]", super.toString(), triangleId);
    }
}
