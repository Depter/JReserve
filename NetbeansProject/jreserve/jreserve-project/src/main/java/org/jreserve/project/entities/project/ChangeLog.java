package org.jreserve.project.entities.project;

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
@EntityRegistration(entityClass=ChangeLog.class)
@Entity
@Table(name="CHANGE_LOG", schema="JRESERVE")
public class ChangeLog implements Serializable {
    private final static long serialVersionUID = 1L;
    
    @Id
    @Column(name="ID")
    private long id;
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATION_TIME", nullable=false)
    private Date creationTime;
    
    @Column(name="LOG_MESSAGE", nullable=false)
    @Type(type="org.hibernate.type.TextType")
    private String logMessage;
    
    protected ChangeLog() {
    }
    
    public ChangeLog(Project project, String log) {
        initProject(project);
        initLogMessage(log);
        creationTime = new Date();
    }
    
    private void initProject(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        this.project = project;
    }
    
    private void initLogMessage(String log) {
        if(log == null)
            throw new NullPointerException("Log message is null!");
        if(log.trim().length() == 0)
            throw new IllegalArgumentException("Log message is empty!");
        this.logMessage = log;
    }

    public long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public String getLogMessage() {
        return logMessage;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof ChangeLog)
            return id == ((ChangeLog)o).id;
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int) id;
    }
    
    @Override
    public String toString() {
        return String.format("%1$tF %1$tT [%2$s]: %3$s",
            creationTime, project, logMessage);
    }
}
