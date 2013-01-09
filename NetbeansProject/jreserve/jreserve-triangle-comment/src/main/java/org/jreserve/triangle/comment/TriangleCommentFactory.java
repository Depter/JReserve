package org.jreserve.triangle.comment;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionTask;

/**
 *
 * @author Peter Decsi
 */
public class TriangleCommentFactory extends SessionTask.AbstractTask<Void> {

    private final static Logger logger = Logger.getLogger(TriangleCommentFactory.class.getName());
    
    private final Commentable commentable;
    private final int accident;
    private final int development;
    private final String commentText;
    
    public TriangleCommentFactory(Commentable commentable, int accident, int development, String comment) {
        this.commentable = commentable;
        this.accident = accident;
        this.development = development;
        this.commentText = comment;
    }

    @Override
    public void doWork(Session session) throws Exception {
        TriangleComment comment = createComment();
        session.persist(comment);
        logger.log(Level.INFO, String.format("TriangleComment created [%d; %d] = %s", accident, development, comment));
        commentable.addComment(comment);
    }
    
    private TriangleComment createComment() {
        PersistentObject owner = commentable.getOwner();
        String user = System.getProperty("user.name");
        return new TriangleComment(owner, accident, development, user, commentText);
    }
}
