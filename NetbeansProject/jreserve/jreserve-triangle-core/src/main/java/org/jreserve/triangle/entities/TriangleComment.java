package org.jreserve.triangle.entities;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(schema="JRESERVE", name="TRIANGLE_COMMENT")
public class TriangleComment extends AbstractPersistentObject implements TriangleCell.Provider, Comparable<TriangleComment>{
    private final static long serialVersionUID = 1L;
    public final static String COMMENT_PROPERTY = "TRIANGLE_COMMENT";
    private final static int NAME_SIZE = 64;
    
    @Column(name="CREATION_DATE", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(name="USER_NAME", nullable=false, length=NAME_SIZE)
    private String userName;
    
    @Column(name="COMMANT_TEXT", nullable=false)
    @Type(type="org.hibernate.type.TextType")
    private String commentText;
    
    @Embedded
    private TriangleCell cell;
    
    protected TriangleComment() {
    }
    
    public TriangleComment(int accident, int development, String user, String comment) {
        this(new TriangleCell(accident, development), user, comment);
    }
    
    public TriangleComment(TriangleCell cell, String comment) {
        this(cell, System.getProperty("user.name"), comment);
    }
    
    public TriangleComment(TriangleCell cell, String user, String comment) {
        this.cell = cell;
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
    
    public String getUserName() {
        return userName;
    }

    @Override
    public TriangleCell getTriangleCell() {
        return cell;
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
    public String toString() {
        return String.format("%1$s [%2$tF %2$tT]: %3$s", 
            userName, creationDate, commentText);
    }

    @Override
    public int compareTo(TriangleComment o) {
        int dif = cell.compareTo(o.cell);
        if(dif != 0) 
            return dif;
        return creationDate.compareTo(o.creationDate);
    }
}
