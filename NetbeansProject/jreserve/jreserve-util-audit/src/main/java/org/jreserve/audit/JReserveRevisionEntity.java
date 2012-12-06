package org.jreserve.audit;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.jreserve.audit.util.JReserveRevisionListener;
import org.jreserve.persistence.EntityRegistration;

/**
 *
 * @author Peter Decsi
 */
@EntityRegistration
@Entity
@RevisionEntity(JReserveRevisionListener.class)
@Table(name="REVISION", schema="JRESERVE")
public class JReserveRevisionEntity {
    
    @Id
    @GeneratedValue
    @RevisionNumber
    @Column(name="REVISION_NUMBER")
    private long revisionNumber;
    
    @RevisionTimestamp
    @Column(name="REVISION_TIME")
    private long revisionTimeStamp;

    @Column(name="USER_NAME")
    private String userName;
    
    public long  getId() {
        return revisionNumber;
    }

    public Date  getRevisionDate() {
        return new Date(revisionTimeStamp);
    }

    public long  getRevisionTimestamp() {
        return revisionTimeStamp;
    }
    
    public void  setRevisionTimestamp(long revisionTimeStamp) {
        this.revisionTimeStamp = revisionTimeStamp;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @Override
    public boolean  equals(Object o) {
        if(o instanceof JReserveRevisionEntity) 
            return equals((JReserveRevisionEntity) o);
        return false;
    }
    
    private boolean equals(JReserveRevisionEntity jrve) {
        return revisionNumber == jrve.revisionNumber &&
               revisionTimeStamp == jrve.revisionTimeStamp;
    }

    @Override
    public int  hashCode() {
        int hash = 31 + (int) revisionNumber;
        return 17 * hash + ((int) revisionTimeStamp);
    }

    @Override
    public String  toString() {
        String msg = "Revision [%d; %tF; %s]";
        return String.format(msg, revisionNumber, getRevisionDate(), userName);
    }
}
