package org.jreserve.triangle.widget;

import java.util.Date;
import org.jreserve.triangle.entities.Comment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleWidgetComment implements Comment {

    private final Date date;
    private final String user;
    private final String comment;
    
    public TriangleWidgetComment(String comment) {
        this(new Date(), System.getProperty("user.name"), comment);
    }
    
    public TriangleWidgetComment(Date date, String user, String comment) {
        this.user = user;
        this.date = date;
        this.comment = comment;
    }
    
    @Override
    public String getUserName() {
        return user;
    }

    @Override
    public Date getCreationDate() {
        return date;
    }

    @Override
    public String getCommentText() {
        return comment;
    }

    @Override
    public String toString() {
        return String.format("[%tF / %s] %s", date, user, comment);
    }
}
