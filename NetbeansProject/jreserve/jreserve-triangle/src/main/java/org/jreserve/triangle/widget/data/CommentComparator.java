package org.jreserve.triangle.widget.data;

import java.util.Comparator;
import org.jreserve.triangle.entities.Comment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CommentComparator implements Comparator<Comment> {

    final static CommentComparator INSTANCE = new CommentComparator();
    
    private CommentComparator() {
    }
    
    @Override
    public int compare(Comment o1, Comment o2) {
        int dif = compare(o1.getCreationDate(), o2.getCreationDate());
        if(dif != 0)
            return dif;
        return compare(o1.getUserName(), o2.getUserName());
    }

    private <T extends Comparable<T>> int compare(T c1, T c2) {
        if(c1 == null)
            return c2 == null ? 0 : 1;
        return c2 == null ? -1 : c1.compareTo(c2);
    }
}
