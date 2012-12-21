package org.jreserve.triangle.comment.visual;

import java.util.List;
import org.jreserve.triangle.comment.TriangleComment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CommentRenderer {

    final static int MAX_ROW_WIDTH = 40;
    private final static String TITLE = "%tF / %s:";
    
    static String renderComments(List<TriangleComment> comments) {
        StringBuilder sb = new StringBuilder("<html>");
        for(int i=0, size=comments.size(); i<size; i++) {
            if(i > 0) sb.append("<br><br>");
            appendComment(sb, comments.get(i));
        }
        return sb.append("</html>").toString();
    }
    
    private static void appendComment(StringBuilder sb, TriangleComment comment) {
        sb.append("<b>").append(getTitle(comment)).append("</b><br>");
        String commentText = comment.getCommentText();
        if(commentText!=null && commentText.length() > 0)
            appendMessage(sb, commentText.toCharArray());
    }
    
    private static String getTitle(TriangleComment comment) {
        java.util.Date date = comment.getCreationDate();
        String user = comment.getUserName();
        return String.format(TITLE, date, user);
    }
    
    private static void appendMessage(StringBuilder sb, char[] comment) {
        int count = 0;
        for(int i=0, size=comment.length; i<size; i++) {
            char c = comment[i];
            
            if(count >= MAX_ROW_WIDTH && i<(size-1) && (Character.isSpaceChar(c) || (count - MAX_ROW_WIDTH) > 10)) {
                sb.append("<br>");
                count=0;
            } else {
                sb.append(c);
                count++;
            }
        }
    }
}