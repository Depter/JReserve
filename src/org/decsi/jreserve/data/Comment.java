package org.decsi.jreserve.data;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Comment implements Comparable<Comment> {
    
    public final static int TRIANGLE_CELL = 0;
    public final static int TRIANGLE = 1;
    
    private int id;
    private Date date;
    private String author;
    private String comment;
    
    public int getId() {
        return id;
    }
    
    public Date getDate() {
        return date;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    @Override
    public int compareTo(Comment c) {
        if(c == null)
            return -1;
        return id - c.getId();
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Comment)
            return compareTo((Comment) o) == 0;
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
}
