package org.jreserve.data.base;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EMFactory {
    
    private final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private final static String URL = "jdbc:derby:memory:unit-testing-jpa";
    
    private static EntityManagerFactory emFactory;
    private static PersistenceUnitUtil pUtil;
    
    public static EntityManager createEntityManager() throws Exception {
        if(emFactory == null)
            createFactory();
        return emFactory.createEntityManager();
    }
    
    private static void createFactory() throws Exception {
        createDatabase();
        Map map = getConnectionProperties();
        emFactory = Persistence.createEntityManagerFactory("BaseDataPU", map);
        pUtil = emFactory.getPersistenceUnitUtil();
    }
    
    private static Map getConnectionProperties() {
        Map map = new HashMap(4);
        map.put("javax.persistence.jdbc.url", URL);
        map.put("javax.persistence.jdbc.driver", DRIVER);
        return map;
    }
    
    private static void createDatabase() throws Exception {
        Class.forName(DRIVER);
        Connection conn = DriverManager.getConnection(URL + ";create=true");
        try {
            createTables(conn);
        } finally {
            conn.close();
        }
    }
    
    private static void createTables(Connection conn) throws SQLException {
        Statement stm = null;
        try {
            stm = conn.createStatement();
            stm.addBatch("CREATE TABLE RESERVE.SEQUENCE_STORE (SEQUENCE_NAME VARCHAR(255) NOT NULL PRIMARY KEY, SEQUENCE_VALUE BIGINT NOT NULL)");
            stm.addBatch("INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.CHANGE_LOG.ID', 0)");
            stm.addBatch("INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.CLAIM_TYPE.ID', 0)");
            stm.addBatch("INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.DATA_TYPE.ID', 0)");
            stm.addBatch("INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.LOB.ID', 0)");
            stm.addBatch("INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.PROJECT.ID', 0)");
            stm.addBatch("INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.TRIANGLE.ID', 0)");
            stm.addBatch("INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.TRIANGLE_COMMENT.ID', 0)");
            stm.addBatch("INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.VECTOR.ID', 0)");
            stm.addBatch("INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.VECTOR_COMMENT.ID', 0)");
            stm.executeBatch();
        } finally {
            stm.close();
        }
    }
    
    static void shutDownDb() throws Exception {
        try {
            DriverManager.getConnection(URL+";shutdown=true").close();
        } catch (SQLNonTransientConnectionException ex) {
            if (ex.getErrorCode() != 45000) {
                throw ex;
            }
        }
    }
    
    static short getId(ClaimType claimType) {
        return (Short) pUtil.getIdentifier(claimType);
    }
}
