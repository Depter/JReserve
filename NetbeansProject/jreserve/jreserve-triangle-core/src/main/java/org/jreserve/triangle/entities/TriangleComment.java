package org.jreserve.triangle.entities;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.Type;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.Comment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Table(schema="JRESERVE", name="TRIANGLE_COMMENT")
public class TriangleComment extends AbstractPersistentObject implements Comment {
    private final static long serialVersionUID = 1L;
    
    private final static int NAME_SIZE = 64;

    @Column(name="ACCIDENT_PERIOD", nullable=false)
    private int accident;
    
    @Column(name="DEVELOPMENT_PERIOD", nullable=false)
    private int development;
    
    @Column(name="CREATION_DATE", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(name="USER_NAME", nullable=false, length=NAME_SIZE)
    private String userName;
    
    @Column(name="COMMANT_TEXT", nullable=false)
    @Type(type="org.hibernate.type.TextType")
    private String commentText;
    
    @Column(name="OWNER_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF, nullable=false)
    private String ownerId;
    
    protected TriangleComment() {
    }
    
    public TriangleComment(PersistentObject owner, int accident, int development, String user, String comment) {
        this.ownerId = owner.getId();
        this.accident = accident;
        this.development = development;
        initUserName(user);
        initComment(comment);
        creationDate = new Date();
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
    
    @Override
    public String getUserName() {
        return userName;
    }

    public int getAccidentPeriod() {
        return accident;
    }
    
    public int getDevelopmentPeriod() {
        return development;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public String getCommentText() {
        return commentText;
    }
    
    public void setCommentText(String comment) {
        initComment(comment);
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    @Override
    public String toString() {
        return String.format("%1$s [%2$tF %2$tT]: %3$s", 
            userName, creationDate, commentText);
    }
}
