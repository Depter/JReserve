package org.jreserve.triangle.comment;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCommentUtil {

    private final static Logger logger = Logger.getLogger(TriangleCommentUtil.class.getName());
    private final static String HQL = "from TriangleComment tc where tc.ownerId = :ownerId";

    public static List<TriangleComment> loadComments(PersistentObject owner) {
        logger.log(Level.FINE, "Loading TriangleCorrections for: {0}", owner);
        Query query = SessionFactory.getCurrentSession().createQuery(HQL);
        query.setString("ownerId", owner.getId());
        return query.list();
    }
    
    public static void save(Session session, List<TriangleComment> original, List<TriangleComment> current) {
        deleteOld(session, original, current);
        saveExisting(session, current);
    }
    
    private static void deleteOld(Session session, List<TriangleComment> original, List<TriangleComment> current) {
        for(TriangleComment comment : original)
            if(!current.contains(comment))
                session.delete(comment);
    }
    
    private static void saveExisting(Session session, List<TriangleComment> currents) {
        for(TriangleComment comment : currents)
            session.saveOrUpdate(comment);
    }
}