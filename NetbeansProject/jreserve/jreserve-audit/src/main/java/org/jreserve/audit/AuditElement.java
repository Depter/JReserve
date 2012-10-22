package org.jreserve.audit;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 */
public class AuditElement {
    
    
    private final Date date;
    private final String user;
    private final String type;
    private final String change;
    
    AuditElement(Date date, String user, String type, String change) {
        this.date = date;
        this.user = user;
        this.type = type;
        this.change = change;
    }

    public Date getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public String getType() {
        return type;
    }

    public String getChange() {
        return change;
    }
    
    @Override
    public String toString() {
        return String.format("Change [%tF, %s, %s]", date, user, change);
    }
}
