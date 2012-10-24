package org.jreserve.data.entities;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractData extends AbstractPersistentObject{

    @Column(name="OWNER_ID", nullable=false, columnDefinition=PersistentObject.COLUMN_DEF)
    private String ownerId;
    
    @Column(name="ACCIDENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date accidentDate;
    
    @Column(name="DEVELOPMENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date developmentDate;
    
    protected AbstractData() {
    }
    
    protected AbstractData(PersistentObject owner, Date accident, Date development) {
        setOwner(owner);
        setAccident(accident);
        setdevelopment(development);
    }
    
    private void setOwner(PersistentObject owner) {
        if(owner == null)
            throw new NullPointerException("Owner can not be null!");
        this.ownerId = owner.getId();
    }
    
    public String getOwnerId() {
        return ownerId;
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
    
    @Override
    public String toString() {
        return String.format("AbstractClaimDate [%s]", 
                getDateString());
    }
    
    protected String getDateString() {
        return String.format("%tF / %tF", 
              accidentDate, developmentDate);
    }
}
