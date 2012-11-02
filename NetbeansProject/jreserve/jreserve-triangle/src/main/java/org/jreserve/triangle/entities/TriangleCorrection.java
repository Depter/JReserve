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
@IdClass(TriangleCorrectionPk.class)
@Table(name="TRIANGLE_CORRECTION", schema="JRESERVE")
public class TriangleCorrection implements Serializable {
    
    private final static String ERR_END_BEFORE_START = 
         "End date '%tF' is before start date '%tF'!";
    
    @Id
    @ManyToOne(optional=false)
    @JoinColumn(name="TRIANGLE_ID", referencedColumnName="ID", columnDefinition=PersistentObject.COLUMN_DEF)
    private Triangle triangle;
    
    @Id
    @Column(name="ACCIDENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date accidentDate;
    
    @Id
    @Column(name="DEVELOPMENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date developmentDate;
    
    @Column(name="CORRECTION", nullable=false)
    private double correction;
    
    protected TriangleCorrection() {
    }
    
    public TriangleCorrection(Triangle triangle, Date accidentDate, Date developmentDate) {
        initTriangle(triangle);
        initAccidentDate(accidentDate);
        initDevelopmentDate(developmentDate);
    }
    
    private void initTriangle(Triangle triangle) {
        if(triangle == null)
            throw new NullPointerException("Triangle is null!");
        this.triangle = triangle;
    }
    
    private void initAccidentDate(Date date) {
        if(date == null)
            throw new NullPointerException("Accident date is null!");
        this.accidentDate = date;
    }
    
    private void initDevelopmentDate(Date date) {
        if(date == null)
            throw new NullPointerException("Development date is null!");
        checkAfterStart(date);
        this.developmentDate = date;
    }
    
    private void checkAfterStart(Date end) {
        if(!end.before(accidentDate))
            return;
        String msg = String.format(ERR_END_BEFORE_START, end, accidentDate);
        throw new IllegalArgumentException(msg);
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public Date getDevelopmentDate() {
        return developmentDate;
    }

    public double getCorrection() {
        return correction;
    }
    
    public void setCorrection(double correction) {
        this.correction = correction;
    }
    
    public Data<Triangle, Double> toData() {
        return new Data<Triangle, Double>(triangle, accidentDate, developmentDate, correction);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleCorrection)
            return equals((TriangleCorrection) o);
        return false;
    }
    
    private boolean equals(TriangleCorrection o) {
        return accidentDate.equals(o.accidentDate) &&
               developmentDate.equals(o.developmentDate);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + accidentDate.hashCode();
        return 17 * hash + developmentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("[%tF / %tF]: %f", 
            accidentDate, developmentDate, correction);
    }
}
