package org.jreserve.data.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.jreserve.data.DataType;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@IdClass(ClaimValuePk.class)
@Table(name="CLAIM_DATA", schema="JRESERVE")
public class ClaimValue implements Serializable {
    private final static long serialVersionUID = 1L;
    
    private final static String ERR_DEV_BEFORE_ACC = 
         "Development date '%tF' is before accident date '%tF'!";
    
    @Id
    @ManyToOne
    @JoinColumn(name="CLAIM_TYPE_ID", referencedColumnName="ID", nullable=false)
    private ClaimType claimType;
    
    @Id
    @Column(name="DATA_TYPE_ID", nullable=false)
    private int dataTypeId;
    
    @Id
    @Column(name="ACCIDENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date accidentDate;
    
    @Id
    @Column(name="DEVELOPMENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date developmentDate;
    
    @Column(name="CLAIM_VALUE", nullable=false)
    private double claimValue;
    
    protected ClaimValue() {
    }
    
    public ClaimValue(ClaimType claimType, DataType dataType, 
            Date accidentDate, Date developmentDate) {
        initClaimType(claimType);
        initDataType(dataType);
        initAccidentDate(accidentDate);
        initDevelopmentDate(developmentDate);
    }
    
    private void initClaimType(ClaimType claimType) {
        if(claimType == null)
            throw new NullPointerException("ClaimType is null!");
        this.claimType = claimType;
    }
    
    private void initDataType(DataType dataType) {
        if(dataType == null)
            throw new NullPointerException("DataType is null!");
        this.dataTypeId = dataType.getDbId();
    }
    
    private void initAccidentDate(Date date) {
        if(date == null)
            throw new NullPointerException("Accident date is null!");
        this.accidentDate = date;
    }
    
    private void initDevelopmentDate(Date date) {
        if(date == null)
            throw new NullPointerException("Development date is null!");
        checkAfterAccident(date);
        this.developmentDate = date;
    }
    
    private void checkAfterAccident(Date date) {
        if(!date.before(accidentDate))
            return;
        String msg = String.format(ERR_DEV_BEFORE_ACC, date, accidentDate);
        throw new IllegalArgumentException(msg);
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public DataType getDataType() {
        return DataType.parse(dataTypeId);
    }

    public Date getAccidentDate() {
        return accidentDate;
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
    public boolean equals(Object o) {
        if(o instanceof ClaimValue)
            return equals((ClaimValue) o);
        return false;
    }
    
    private boolean equals(ClaimValue o) {
        return claimType.equals(o.claimType) &&
               dataTypeId == o.dataTypeId &&
               accidentDate.equals(o.accidentDate) &&
               developmentDate.equals(o.developmentDate);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + claimType.hashCode();
        hash = 17 * hash + dataTypeId;
        hash = 17 * hash + accidentDate.hashCode();
        return 17 * hash + developmentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format(
            "ClaimData [%s; %s; %tF; %tF; %f]",
            claimType.getName(), DataType.parse(dataTypeId),
            accidentDate, developmentDate, claimValue);
    }
}