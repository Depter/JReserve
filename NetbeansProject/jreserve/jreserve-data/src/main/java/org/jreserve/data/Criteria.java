package org.jreserve.data;

import java.util.Date;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Criteria {
    
    private ClaimType claimType;
    private ProjectDataType dataType = null;
    private Date fromAccidentDate;
    private Date toAccidentDate;
    private Date fromDevelopmentDate;
    private Date toDevelopmentDate;

    public Criteria(ClaimType claimType) {
        if(claimType == null)
            throw new NullPointerException("ClaimType can not be null!");
        this.claimType = claimType;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public ProjectDataType getDataType() {
        return dataType;
    }

    public Criteria setDataType(ProjectDataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public Date getFromAccidentDate() {
        return fromAccidentDate;
    }
    
    public Criteria setFromAccidentDate(Date fromDate) {
        if(fromDate != null && toAccidentDate != null && fromDate.after(toAccidentDate))
            throw getDateException(fromDate, toAccidentDate);
        this.fromAccidentDate = fromDate;
        return this;
    }

    private IllegalArgumentException getDateException(Date from, Date to) {
        String msg = "From date can not be after to date! [%tF > %tF]";
        msg = String.format(msg, from, to);
        return new IllegalArgumentException(msg);
    }

    public Date getToAccidentDate() {
        return toAccidentDate;
    }

    public Criteria setToAccidentDate(Date toDate) {
        if(fromAccidentDate != null && toDate != null && fromAccidentDate.after(toDate))
            throw getDateException(fromAccidentDate, toDate);
        this.toAccidentDate = toDate;
        return this;
    }

    public Date getFromDevelopmentDate() {
        return fromDevelopmentDate;
    }
    
    public Criteria setFromDevelopmentDate(Date fromDate) {
        if(fromDate != null && toDevelopmentDate != null && fromDate.after(toDevelopmentDate))
            throw getDateException(fromDate, toDevelopmentDate);
        this.fromDevelopmentDate = fromDate;
        return this;
    }

    public Date getToDevelopmentDate() {
        return toDevelopmentDate;
    }

    public Criteria setToDevelopmentDate(Date toDate) {
        if(fromDevelopmentDate != null && toDate != null && fromDevelopmentDate.after(toDate))
            throw getDateException(fromDevelopmentDate, toDate);
        this.toDevelopmentDate = toDate;
        return this;
    }

    @Override
    public String toString() {
        String msg = "Criteria: ClaimType = %s; type = %s; "+
                "fromAccident = %tF; toAccidnet = %tF; "+
                "fromDevelopment = %tF; toDevelopment = %tF";
        return String.format(msg, claimType, dataType, 
                fromAccidentDate, toAccidentDate,
                fromDevelopmentDate, toDevelopmentDate);
    }
}
