package org.jreserve.data.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
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
    
    @Override
    public String toString() {
        return String.format("DataComment [%s, %s]",
                getDateString(), comment);
    }
}
