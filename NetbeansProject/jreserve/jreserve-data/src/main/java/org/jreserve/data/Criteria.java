package org.jreserve.data;

import java.util.Date;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Criteria {
    
    private ClaimType ct;
    private DataType dataType = null;
    private Date fromDate;
    private Date toDate;

    public Criteria(ClaimType ct) {
        if(ct == null)
            throw new NullPointerException("ClaimType can not be null!");
        this.ct = ct;
    }

    public ClaimType getClaimType() {
        return ct;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Criteria setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public Date getFromDate() {
        return fromDate;
    }
    
    public Criteria setFromDate(Date fromDate) {
        if(fromDate != null && toDate != null && fromDate.after(toDate))
            throw getDateException(fromDate, toDate);
        this.fromDate = fromDate;
        return this;
    }

    private IllegalArgumentException getDateException(Date from, Date to) {
        String msg = "From date can not be after to date! [%tF > %tF]";
        msg = String.format(msg, from, to);
        return new IllegalArgumentException(msg);
    }

    public Date getToDate() {
        return toDate;
    }

    public Criteria setToDate(Date toDate) {
        if(fromDate != null && toDate != null && fromDate.after(toDate))
            throw getDateException(fromDate, toDate);
        this.toDate = toDate;
        return this;
    }

    @Override
    public String toString() {
        String msg = "Criteria: claim = %s; type = %s; from = %tF; to = %tF";
        return String.format(msg, ct, dataType, fromDate, toDate);
    }
}
