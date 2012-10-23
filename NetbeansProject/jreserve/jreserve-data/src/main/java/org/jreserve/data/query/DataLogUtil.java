package org.jreserve.data.query;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.DataLog;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "# {0} - dbId",
    "# {1} - name",
    "MSG.DataLogUtil.Imported=Data imported for \"{0} - {1}\".",
    "# {0} - dbId",
    "# {1} - name",
    "MSG.DataLogUtil.Deleted=Data deleted for \"{0} - {1}\"."
})
public class DataLogUtil {

    private final static String SQL = "SELECT l FROM DataLog l WHERE l.dataType.id = :dtId";
    
    static DataLog getDataLog(Session session, ProjectDataType dataType) {
        DataLog log = loadFromDb(session, dataType.getId());
        if(log != null)
            return log;
        return createDataLog(session, dataType);
    }

    private static DataLog loadFromDb(Session session, String id) {
        Query query = session.createQuery(SQL);
        query.setString("dtId", id);
        return (DataLog) query.uniqueResult();
    }
    
    private static DataLog createDataLog(Session session, ProjectDataType dataType) {
        DataLog log = new DataLog(dataType);
        session.persist(log);
        return log;
    }
    
    public static void logImport(Session session, ProjectDataType dataType) {
        int dbId = dataType.getDbId();
        String name = dataType.getName();
        String msg = Bundle.MSG_DataLogUtil_Imported(dbId, name);
        log(session, dataType, msg);
    }
    
    public static void logDeletion(Session session, ProjectDataType dataType) {
        int dbId = dataType.getDbId();
        String name = dataType.getName();
        String msg = Bundle.MSG_DataLogUtil_Deleted(dbId, name);
        log(session, dataType, msg);
    }
    
    private static void log(Session session, ProjectDataType dataType, String msg) {
        DataLog log = getDataLog(session, dataType);
        log.setLog(msg);
        session.update(log);
    }
}
