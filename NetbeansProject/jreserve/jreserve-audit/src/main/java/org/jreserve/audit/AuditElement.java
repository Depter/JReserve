package org.jreserve.audit;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 */
public class AuditElement {
    
    private final Date date;
    private final String user;
    private final String change;

    public AuditElement(Date date, String user, String change) {
        this.date = date;
        this.user = user;
        this.change = change;
    }

    public String getChange() {
        return change;
    }

    public Date getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }
    
    @Override
    public String toString() {
        return String.format("Change [%tF, %s, %s]", date, user, change);
    }
    
}
