package org.jreserve.audit;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AuditElementFactory {

    private Date date;
    private String user;
    private String type;
    private String change;
    
    public AuditElementFactory() {
    }

    public AuditElementFactory setEntity(JReserveRevisionEntity entity) {
        date = entity.getRevisionDate();
        user = entity.getUserName();
        return this;
    }
    
    public AuditElementFactory setChange(String change) {
        this.change = change;
        return this;
    }

    public AuditElementFactory setDate(Date date) {
        this.date = date;
        return this;
    }

    public AuditElementFactory setType(String type) {
        this.type = type;
        return this;
    }

    public AuditElementFactory setUser(String user) {
        this.user = user;
        return this;
    }
    
    public AuditElement build() {
        checkState();
        return new AuditElement(date, user, type, change);
    }
    
    private void checkState() {
        checkDate();
        checkUser();
        checkType();
        checkChange();
    }
    
    private void checkDate() {
        if(date == null)
            throw new IllegalStateException("Date is not set!");
    }
    
    private void checkUser() {
        if(user == null)
            throw new IllegalStateException("User is not set!");
    }
    
    private void checkType() {
        if(type == null)
            throw new IllegalStateException("Type is not set!");
    }
    
    private void checkChange() {
        if(change == null)
            throw new IllegalStateException("Change is not set!");
    }
}
