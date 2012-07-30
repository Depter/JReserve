package org.jreserve.project.entities.project;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.Type;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(entityClass=VectorComment.class)
@Entity
@Table(name="VECTOR_COMMENT", schema="JRESERVE")
public class VectorComment implements Serializable {
    private final static long serialVersionUID = 1L;
    
    private final static int NAME_SIZE = 64;
    
    @Id
    @Column(name="ID")
    private long id;
    
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="VECTOR_ID", referencedColumnName="ID", nullable=false)
    private Vector vector;
    
    @Column(name="ACCIDENT_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date accidentDate;
    
    @Column(name="USER_NAME", nullable=false, length=NAME_SIZE)
    private String userName;
    
    @Column(name="CREATION_DATE", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(name="COMMANT_TEXT", nullable=false)
    @Type(type="org.hibernate.type.TextType")
    private String commentText;
    
    protected VectorComment() {
    }
    
    public VectorComment(Vector vector, Date accidentDate, String user, String comment) {
        initVector(vector);
        initAccidentDate(accidentDate);
        initUserName(user);
        initComment(comment);
        creationDate = new Date();
    }
    
    private void initVector(Vector vector) {
        if(vector == null)
            throw new NullPointerException("Vector is null!");
        this.vector = vector;
    }
    
    private void initAccidentDate(Date date) {
        if(date == null)
            throw new NullPointerException("Date is null!");
        this.accidentDate = date;
    }
    
    private void initUserName(String user) {
        PersistenceUtil.checkVarchar(user, NAME_SIZE);
        this.userName = user;
    }
    
    private void initComment(String comment) {
        if(comment==null)
            throw new NullPointerException("Comment can not be null!");
        this.commentText = comment;
    }

    public long getId() {
        return id;
    }

    public Vector getVector() {
        return vector;
    }
    
    void setVector(Vector vector) {
        if(this.vector != null)
            this.vector.removeComment(this);
        this.vector = vector;
    }
    
    public String getUserName() {
        return userName;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getCommentText() {
        return commentText;
    }
    
    public void setCommentText(String comment) {
        initComment(comment);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof VectorComment)
            return id == ((VectorComment)o).id;
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int) id;
    }
    
    @Override
    public String toString() {
        return String.format("%1$s [%2$tF %2$tT]: %3$s", 
            userName, creationDate, commentText);
    }
}
