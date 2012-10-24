package org.jreserve.data;

import java.util.Date;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataCriteria<T extends PersistentObject> {
    
    public static enum EQT {
        GE,
        GT,
        EQ,
        LT,
        LE
    };
    
    private T owner = null;
    
    private Date fromAccidentDate;
    private EQT fromAccidentEqt = EQT.GE;
    
    private Date toAccidentDate;
    private EQT toAccidentEqt = EQT.LE;
    
    private Date fromDevelopmentDate;
    private EQT fromDevelopmentEqt = EQT.GE;
    
    private Date toDevelopmentDate;
    private EQT toDevelopmentEqt = EQT.LE;

    public DataCriteria(T owner) {
        if(owner == null)
            throw new NullPointerException("Owner can not be null!");
        this.owner = owner;
    }

    public T getOwner() {
        return owner;
    }

    public DataCriteria setOwner(T owner) {
        if(owner == null)
            throw new NullPointerException("Owner can not be null!");
        this.owner = owner;
        return this;
    }
    
    public String getOwnerId() {
        return owner.getId();
    }
    
    public Date getFromAccidentDate() {
        return fromAccidentDate;
    }
    
    public DataCriteria setFromAccidentDate(Date fromDate) {
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

    public EQT getFromAccidentEqt() {
        return fromAccidentEqt;
    }
    
    public DataCriteria setFromAccidentEqt(EQT eqt) {
        this.fromAccidentEqt = eqt==null? EQT.GE : eqt;
        return this;
    }

    public Date getToAccidentDate() {
        return toAccidentDate;
    }

    public DataCriteria setToAccidentDate(Date toDate) {
        if(fromAccidentDate != null && toDate != null && fromAccidentDate.after(toDate))
            throw getDateException(fromAccidentDate, toDate);
        this.toAccidentDate = toDate;
        return this;
    }

    public EQT getToAccidentEqt() {
        return toAccidentEqt;
    }
    
    public DataCriteria setToAccidentEqt(EQT eqt) {
        this.toAccidentEqt = eqt==null? EQT.LE : eqt;
        return this;
    }

    public Date getFromDevelopmentDate() {
        return fromDevelopmentDate;
    }
    
    public DataCriteria setFromDevelopmentDate(Date fromDate) {
        if(fromDate != null && toDevelopmentDate != null && fromDate.after(toDevelopmentDate))
            throw getDateException(fromDate, toDevelopmentDate);
        this.fromDevelopmentDate = fromDate;
        return this;
    }

    public EQT getFromDevelopmentEqt() {
        return fromDevelopmentEqt;
    }
    
    public DataCriteria setFromDevelopmentEqt(EQT eqt) {
        this.fromDevelopmentEqt = eqt==null? EQT.GE : eqt;
        return this;
    }

    public Date getToDevelopmentDate() {
        return toDevelopmentDate;
    }

    public DataCriteria setToDevelopmentDate(Date toDate) {
        if(fromDevelopmentDate != null && toDate != null && fromDevelopmentDate.after(toDate))
            throw getDateException(fromDevelopmentDate, toDate);
        this.toDevelopmentDate = toDate;
        return this;
    }

    public EQT getToDevelopmentEqt() {
        return toDevelopmentEqt;
    }
    
    public DataCriteria setToDevelopmentEqt(EQT eqt) {
        this.toDevelopmentEqt = eqt==null? EQT.LE : eqt;
        return this;
    }

    @Override
    public String toString() {
        String msg = "Criteria: Owner = %s; "+
                "fromAccident = %tF; toAccidnet = %tF; "+
                "fromDevelopment = %tF; toDevelopment = %tF";
        return String.format(msg, owner.getId(),
                fromAccidentDate, toAccidentDate,
                fromDevelopmentDate, toDevelopmentDate);
    }
}
