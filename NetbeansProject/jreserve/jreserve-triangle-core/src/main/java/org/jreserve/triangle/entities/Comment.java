package org.jreserve.triangle.entities;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Comment {
    
    public String getUserName();

    public Date getCreationDate();

    public String getCommentText();
    
}