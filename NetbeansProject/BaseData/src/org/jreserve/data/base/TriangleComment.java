package org.jreserve.data.base;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Entity
@Table(name = "TRIANGLE_COMMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TriangleComment.findAll", query = "SELECT t FROM TriangleComment t"),
    @NamedQuery(name = "TriangleComment.findById", query = "SELECT t FROM TriangleComment t WHERE t.id = :id")
})
public class TriangleComment extends TriangleCoordinate implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name="org.jreserve.data.base.TriangleComment", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.TRIANGLE_COMMENT.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.base.TriangleComment")
    private long id;
    
    @Column(name = "USER_NAME", nullable = false)
    private String userName;
    
    @Column(name = "TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    
    @Column(name = "COMMENT", nullable = false)
    private String comment;
    
    @JoinColumn(name = "TRIANGLE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Triangle triangle;

    public TriangleComment() {
    }

    public TriangleComment(short accidentYear, short accidentMonth, short developmentYear, short developmentMonth, String userName, String comment) {
        super(accidentYear, accidentMonth, developmentYear, developmentMonth);
        this.userName = userName;
        this.time = new Date();
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTime() {
        return time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public void setTriangle(Triangle triangle) {
        this.triangle = triangle;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TriangleComment))
            return false;
        TriangleComment other = (TriangleComment) object;
        return id == other.id;
    }

    @Override
    public String toString() {
        String str = "Comment [%s]: %s";
        return String.format(str, super.toString(), comment);
    }
}
