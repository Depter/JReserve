package org.jreserve.triangle.data;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 */
public interface Comment {

    public String getUserName();
    
    public Date getCreationDate();
    
    public String getCommentText();

}
