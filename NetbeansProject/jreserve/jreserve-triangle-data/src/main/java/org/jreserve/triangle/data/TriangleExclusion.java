package org.jreserve.triangle.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
@EntityRegistration
@Entity
@Table(schema="JRESERVE", name="FACTOR_EXCLUSIONS")
public class TriangleExclusion extends AbstractPersistentObject implements Serializable {
    
    private final static String ERR_END_BEFORE_START = 
         "End date '%tF' is before start date '%tF'!";
    
    @Column(name="ACCIDENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date accidentDate;
    
    @Column(name="DEVELOPMENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date developmentDate;
    
    @Column(name="OWNER_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF, nullable=false)
    private String ownerId;
    
    protected TriangleExclusion() {
    }
    
    public TriangleExclusion(PersistentObject owner, Date accidentDate, Date developmentDate) {
        this.ownerId = owner.getId();
        initAccidentDate(accidentDate);
        initDevelopmentDate(developmentDate);
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

    public Date getAccidentDate() {
        return accidentDate;
    }

    public Date getDevelopmentDate() {
        return developmentDate;
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleExclusion)
            return equals((TriangleExclusion) o);
        return false;
    }
    
    private boolean equals(TriangleExclusion o) {
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
        return String.format("FactorExclusion [%tF / %tF]", 
            accidentDate, developmentDate);
    }
}
