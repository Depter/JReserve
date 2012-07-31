package org.jreserve.project.entities.project;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.jreserve.persistence.EntityRegistration;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@IdClass(VectorCorrectionPk.class)
@Table(name="VECTOR_CORRECTION", schema="JRESERVE")
public class VectorCorrection implements Serializable {
    
    @Id
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="VECTOR_ID", referencedColumnName="ID", nullable=false)
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
    
    void setVector(Vector vector) {
        if(this.vector != null)
            this.vector.removeCorrection(this);
        this.vector = vector;
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
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof VectorCorrection)
            return equals((VectorCorrection) o);
        return false;
    }
    
    private boolean equals(VectorCorrection o) {
        return vector.equals(o.vector) &&
               accidentDate.equals(o.accidentDate);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + vector.hashCode();
        return 17 * hash + accidentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("[%tF]: %f", accidentDate, correction);
    }
}
