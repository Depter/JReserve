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
@Table(name = "VECTOR_COMMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VectorComment.findAll", query = "SELECT v FROM VectorComment v"),
    @NamedQuery(name = "VectorComment.findById", query = "SELECT v FROM VectorComment v WHERE v.id = :id")
})
public class VectorComment extends VectorCoordinate implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name="org.jreserve.data.base.VectorComment", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.VECTOR_COMMENT.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.base.VectorComment")
    private long id;

    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    @Column(name = "TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    
    @Column(name = "COMMENT", nullable = false)
    private String comment;
    
    @JoinColumn(name = "VECTOR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Vector vector;

    public VectorComment() {
    }

    public VectorComment(short accidentYear, short accidentMonth, String userName, String comment) {
        super(accidentYear, accidentMonth);
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

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VectorComment))
            return false;
        VectorComment other = (VectorComment) object;
        return id == other.id;
    }

    @Override
    public String toString() {
        String str = "Comment [%s]: %s";
        return String.format(str, super.toString()  , comment);
    }

}
