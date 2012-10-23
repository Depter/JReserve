package org.jreserve.data.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 */
@EntityRegistration
@Audited
@Entity
@Table(name="DATA_LOG", schema="JRESERVE")
public class DataLog extends AbstractPersistentObject {
    private final static long serialVersionUID = 1L;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="DATA_TYPE_ID", referencedColumnName="ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF)
    private ProjectDataType dataType;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="CLAIM_TYPE_ID", referencedColumnName="ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF)
    private ClaimType claimType;
    
    @Column(name="LOG")
    @Type(type="org.hibernate.type.TextType")
    private String log;
    
    protected DataLog() {
    }
    
    public DataLog(ProjectDataType dataType) {
        if(dataType == null)
            throw new NullPointerException("Data type can not be null!");
        this.dataType = dataType;
        this.claimType = dataType.getClaimType();
    }
    
    public ClaimType getClaimType() {
        return claimType;
    }
    
    public ProjectDataType getDataType() {
        return dataType;
    }
    
    public String getLog() {
        return log;
    }
    
    public void setLog(String log) {
        this.log = log;
    }
}
