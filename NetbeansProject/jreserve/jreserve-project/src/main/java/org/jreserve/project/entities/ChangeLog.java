package org.jreserve.project.entities;

import java.util.Date;
import javax.persistence.*;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Table(name="CHANGE_LOG", schema="JRESERVE")
@Messages({
    "LBL.LogType.Project=Project"
})
public class ChangeLog extends AbstractPersistentObject implements Comparable<ChangeLog> {
    private final static long serialVersionUID = 1L;
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATION_TIME", nullable=false)
    private Date creationTime;
    
    @Column(name="USER_NAME", nullable=false)
    private String userName;
    
    @Column(name="LOG_MESSAGE", nullable=false)
    @org.hibernate.annotations.Type(type="org.hibernate.type.TextType")
    private String logMessage;
    
    @Column(name="TYPE_ID", nullable=false)
    private int typeId;
    
    protected ChangeLog() {
    }
    
    public ChangeLog(Project project, Type type, String log) {
        setProject(project);
        this.typeId = type.getDbId();
        initLogMessage(log);
        creationTime = new Date();
        initUserName();
    }
    
    private void setProject(Project project) {
        checkState(project);
        this.project = project;
    }
    
    private void checkState(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
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

    public Project getProject() {
        return project;
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
    
    public Type getType() {
        return parseType(typeId);
    }
    
    public void setType(Type type) {
        this.typeId = type.getDbId();
    }
    
    @Override
    public int compareTo(ChangeLog o) {
        if(o == null)
            return -1;
        
        int dif = creationTime.compareTo(o.creationTime);
        if(dif != 0)
            return dif;
        String id = getId();
        return id==null? -1 : id.compareTo(o.getId());
    }
    
    @Override
    public String toString() {
        return String.format("%1$s (%2$tF %2$tT) [%3$s]: %4$s",
            userName, creationTime, project, logMessage);
    }
    
    public static enum Type {
        PROJECT(1, Bundle.LBL_LogType_Project());
        
        private final int dbId;
        private final String userName;
        
        private Type(int dbId, String userName) {
            this.dbId = dbId;
            this.userName = userName;
        }
        
        public int getDbId() {
            return dbId;
        }
        
        public String getUserName() {
            return userName;
        }
    }
    
    private static Type parseType(int dbId) {
        for(Type type : Type.values())
            if(type.dbId == dbId)
                return type;
        String msg = String.format("Unknwon database id for ChangeLog.Type: %d", dbId);
        throw new IllegalArgumentException(msg);
    }
}
