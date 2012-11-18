package org.jreserve.data.entities;

import java.util.Date;
import javax.persistence.*;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Table(name="CLAIM_DATA", schema="JRESERVE")
public class ClaimValue extends AbstractPersistentObject implements Comparable<ClaimValue> {
    private final static long serialVersionUID = 1L;
    
    private final static String ERR_DEV_BEFORE_ACC = 
         "Development date '%tF' is before accident date '%tF'!";

    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="DATA_TYPE_ID", referencedColumnName="ID", nullable=false, columnDefinition=PersistentObject.COLUMN_DEF)
    private ProjectDataType dataType;
    
    @Column(name="ACCIDENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date accidentDate;
    
    @Column(name="DEVELOPMENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date developmentDate;
    
    @Column(name="CLAIM_VALUE", nullable=false)
    private double claimValue;
    
    protected ClaimValue() {
    }
    
    public ClaimValue(ProjectDataType dt, Date accident, Date development) {
        setProjectDataType(dt);
        setAccident(accident);
        setdevelopment(development);
    }
    
    public ClaimValue(ProjectDataType dt, Date accident, Date development, double value) {
        this(dt, accident, development);
        this.claimValue = value;
    }
    
    private void setProjectDataType(ProjectDataType dataType) {
        if(dataType == null)
            throw new NullPointerException("ProjectDataType can not be null!");
        this.dataType = dataType;
    }
    
    public ProjectDataType getDataType() {
        return dataType;
    }
    
    private void setAccident(Date date) {
        if(date == null)
            throw new NullPointerException("Accident date can not be null!");
        this.accidentDate = date;
    }
    
    public Date getAccidentDate() {
        return accidentDate;
    }
    
    private void setdevelopment(Date date) {
        if(date == null)
            throw new NullPointerException("Development date can not be null!");
        if(date.before(accidentDate))
            throw new IllegalArgumentException(String.format("Development date '%tF' is before accident date '%tF'!", date, accidentDate));
        this.developmentDate = date;
    }
    
    public Date getDevelopmentDate() {
        return developmentDate;
    }

    public double getClaimValue() {
        return claimValue;
    }
    
    public void setClaimValue(double value) {
        this.claimValue = value;
    }
    
    @Override
    public String toString() {
        return String.format("ClaimValue [%s]: %d", 
                getDateString(), claimValue);
    }
    
    protected String getDateString() {
        return String.format("%tF / %tF", 
              accidentDate, developmentDate);
    }

    @Override
    public int compareTo(ClaimValue o) {
        if(o == null) return -1;
        int dif = this.dataType.compareTo(o.dataType);
        if(dif != 0) return dif;
    
        dif = this.accidentDate.compareTo(o.accidentDate);
        if(dif != 0) return dif;
        
        return this.developmentDate.compareTo(o.developmentDate);
    }
    
}
