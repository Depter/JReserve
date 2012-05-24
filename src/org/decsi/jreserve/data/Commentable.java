package org.decsi.jreserve.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class Commentable {
    
    private List<Comment> comments = new ArrayList<>();
    
    public List<Comment> getComments() {
        return comments;   
    }
    
    public void addComment(Comment comment) {
        if(comment == null || comments.contains(comment))
            return;
        comments.add(comment);
        Collections.sort(comments);
    }
    
    public abstract int getCommentTypeId();
}
