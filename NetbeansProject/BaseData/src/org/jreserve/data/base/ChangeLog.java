package org.jreserve.data.base;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Entity
@Table(name = "CHANGE_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChangeLog.findAll", query = "SELECT c FROM ChangeLog c"),
    @NamedQuery(name = "ChangeLog.findById", query = "SELECT c FROM ChangeLog c WHERE c.id = :id")
})
public class ChangeLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name="org.jreserve.data.base.ChangeLog", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.CHANGE_LOG.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.base.ChangeLog")
    private long id;
    
    @Column(name = "TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    
    @Column(name = "CHANGE", nullable = false)
    private String change;
    
    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Project project;

    public ChangeLog() {
    }

    public ChangeLog(String change) {
        this.creationTime = new Date();
        this.change = change;
    }

    public long getId() {
        return id;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public String getChange() {
        return change;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ChangeLog))
            return false;
        ChangeLog other = (ChangeLog) object;
        return id == other.id;
    }

    @Override
    public String toString() {
        String str = "ChangeLog [%1$tY-%1$tm-%1td %1$tH:%1$tM:%1$tS]: %2$s";
        return String.format(str, creationTime, change);
    }
}
