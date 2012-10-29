package org.jreserve.data;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import org.jreserve.data.entities.AbstractData;
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
@Table(name="DATA_COMMENT", schema="JRESERVE")
public class DataComment extends AbstractData {

    @Column(name="COMMENT", nullable=false)
    private String comment;
    
    @Column(name="USER_NAME", nullable=false)
    private String userName = System.getProperty("user.name");
    
    @Column(name="CREATION_DATE", nullable=false)
    private long creationTimeStamp = new Date().getTime();
    
    protected DataComment() {
    }
    
    public DataComment(PersistentObject owner, Date accident, Date development, String comment) {
        super(owner, accident, development);
        this.comment = comment;
    }
    
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Date getCreationDate() {
        return new Date(creationTimeStamp);
    }
    
    public String getUserName() {
        return userName;
    }
    
    @Override
    public String toString() {
        return String.format("DataComment [%s, %s]",
                getDateString(), comment);
    }
}
