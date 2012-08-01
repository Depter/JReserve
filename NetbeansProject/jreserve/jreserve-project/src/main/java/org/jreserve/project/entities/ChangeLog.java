package org.jreserve.project.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.Type;
import org.jreserve.persistence.EntityRegistration;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(generateId=true)
@Entity
@Table(name="CHANGE_LOG", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.project.entities.ChangeLog",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    pkColumnValue="org.jreserve.project.entities.ChangeLog"
)
public class ChangeLog implements Serializable, Comparable<ChangeLog> {
    private final static long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.project.entities.ChangeLog")
    @Column(name="ID")
    private long id;
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATION_TIME", nullable=false)
    private Date creationTime;
    
    @Column(name="USER_NAME", nullable=false)
    private String userName;
    
    @Column(name="LOG_MESSAGE", nullable=false)
    @Type(type="org.hibernate.type.TextType")
    private String logMessage;
    
    protected ChangeLog() {
    }
    
    public ChangeLog(String log) {
        initLogMessage(log);
        creationTime = new Date();
        initUserName();
    }
    
    private void initLogMessage(String log) {
        if(log == null)
            throw new NullPointerException("Log message is null!");
        if(log.trim().length() == 0)
            throw new IllegalArgumentException("Log message is empty!");
        this.logMessage = log;
    }

    private void initUserName() {
        userName = System.getProperty("user.name");
        if(userName == null)
            userName = "unknown";
    }
    
    public long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }
    
    void setProject(Project project) {
        checkState(project);
        this.project = project;
    }
    
    private void checkState(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        if(this.project != null)
            throw new IllegalStateException("ChangeLog is already added to project: "+this.project);
    }
    
    public Date getCreationTime() {
        return creationTime;
    }

    public String getUserName() {
        return userName;
    }
    
    public String getLogMessage() {
        return logMessage;
    }
    
    @Override
    public int compareTo(ChangeLog o) {
        if(o == null)
            return -1;
        int dif = creationTime.compareTo(o.creationTime);
        return dif!=0? dif : compareId(o);
    }
    
    private int compareId(ChangeLog o) {
        if(id < o.id)
            return -1;
        return id==o.id? 0 : 1;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof ChangeLog)
            return compareTo((ChangeLog)o) == 0;
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + creationTime.hashCode();
        return 17 * hash + (int) id;
    }
    
    @Override
    public String toString() {
        return String.format("%1$s (%2$tF %2$tT) [%3$s]: %4$s",
            userName, creationTime, project, logMessage);
    }
}
