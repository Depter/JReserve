package org.jreserve.triangle.comment;

import java.util.List;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.util.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCommentUtil {
    
    public static TriangleComment createComment(CommentableTriangle triangle, TriangleCell cell, String comment) {
        return createComment(triangle, 
                cell.getAccident(), 
                cell.getDevelopment(), 
                comment);
    }
    
    public static TriangleComment createComment(CommentableTriangle triangle, int accident, int development, String comment) {
        String userName = System.getProperty("user.name");
        return triangle.createComment(
                accident, development, 
                userName, comment);
    }
    
    public static List<TriangleComment> getComments(CommentableTriangle triangle, TriangleCell cell) {
        return getComments(triangle, cell.getAccident(), cell.getDevelopment());
    }
    
    public static List<TriangleComment> getComments(CommentableTriangle triangle, int accident, int development) {
        return TriangleUtil.filterValues(
                triangle.getComments(), 
                accident, development);
    }
    
    private TriangleCommentUtil() {}
}
