package org.jreserve.triangle.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.jreserve.data.Data;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Audited
@IdClass(VectorCorrectionPk.class)
@Table(name="VECTOR_CORRECTION", schema="JRESERVE")
public class VectorCorrection implements Serializable {
    
    @Id
    @ManyToOne(optional=false)
    @JoinColumn(name="VECTOR_ID", referencedColumnName="ID", columnDefinition=PersistentObject.COLUMN_DEF)
    private Vector vector;
    
    @Id
    @Column(name="ACCIDENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date accidentDate;
    
    @Column(name="CORRECTION", nullable=false)
    private double correction;
    
    protected VectorCorrection() {
    }
    
    public VectorCorrection(Vector vector, Date accidentDate) {
        initVector(vector);
        initAccidentDate(accidentDate);
    }
    
    private void initVector(Vector vector) {
        if(vector == null)
            throw new NullPointerException("Vector is null!");
        this.vector = vector;
    }
    
    private void initAccidentDate(Date date) {
        if(date == null)
            throw new NullPointerException("Accident date is null!");
        this.accidentDate = date;
    }

    public Vector getVector() {
        return vector;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public double getCorrection() {
        return correction;
    }
    
    public void setCorrection(double correction) {
        this.correction = correction;
    }
    
    public Data<Vector, Double> toData() {
        return new Data<Vector, Double>(vector, accidentDate, accidentDate, correction);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof VectorCorrection)
            return equals((VectorCorrection) o);
        return false;
    }
    
    private boolean equals(VectorCorrection o) {
        return accidentDate.equals(o.accidentDate);
    }
    
    @Override
    public int hashCode() {
        return 31 + accidentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("[%tF]: %f", accidentDate, correction);
    }
}
