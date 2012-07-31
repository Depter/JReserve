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
@IdClass(TriangleCorrectionPk.class)
@Table(name="TRIANGLE_CORRECTION", schema="JRESERVE")
public class TriangleCorrection implements Serializable {
    
    private final static String ERR_END_BEFORE_START = 
         "End date '%tF' is before start date '%tF'!";
    
    @Id
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="TRIANGLE_ID", referencedColumnName="ID", nullable=false)
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
        initDevelopmentDate(accidentDate);
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
        this.accidentDate = date;
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
    
    void setTriangle(Triangle triangle) {
        if(this.triangle != null)
            this.triangle.removeCorrection(this);
        this.triangle = triangle;
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
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleCorrection)
            return equals((TriangleCorrection) o);
        return false;
    }
    
    private boolean equals(TriangleCorrection o) {
        return triangle.equals(o.triangle) &&
               accidentDate.equals(o.accidentDate) &&
               developmentDate.equals(o.developmentDate);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + triangle.hashCode();
        hash = 17 * hash + accidentDate.hashCode();
        return 17 * hash + developmentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("[%tF / %tF]: %f", 
            accidentDate, developmentDate, correction);
    }
}
