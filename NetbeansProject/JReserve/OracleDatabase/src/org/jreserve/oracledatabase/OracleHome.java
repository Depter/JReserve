package org.jreserve.oracledatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.oracledatabase.file.OracleJsonReader;
import org.jreserve.oracledatabase.file.OracleJsonWriter;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OracleHome {
   
    private final static Logger logger = Logger.getLogger(OracleHome.class.getName());
    
    private final static String ORACLE_HOME = "oracledbs"; 
    private final static String JSON_FILE = "json";
    
    private static Set<OracleDatabase> databases = null;
    private static FileObject home;
   
    static List<OracleDatabase> getDatabases() throws IOException {
        if(databases == null)
            loadDatabases();
        return getOpenedDatabases(true);
    }
   
    private static List<OracleDatabase> getOpenedDatabases(boolean isOpened) {
        List<OracleDatabase> opened = new ArrayList<OracleDatabase>();
        for(OracleDatabase db : databases)
            if(db.isOpened() == isOpened)
                opened.add(db);
        return opened;
    }
    
    public static List<OracleDatabase> getClosedDatabases() throws IOException {
        if(databases == null)
            loadDatabases();
        return getOpenedDatabases(false);
    }
    
    private static void loadDatabases() throws IOException {
        initHomeDirectory();
        loadDatabasesFromFile();
    }
   
    private static void initHomeDirectory() throws IOException {
        try {
            FileObject configHome = FileUtil.getConfigRoot();
            home = FileUtil.createFolder(configHome, ORACLE_HOME);
            logger.log(Level.CONFIG, "Oracle home directory set to: %s", home);
        } catch (IOException ex) {
            IOException myEx = new IOException("Unable to initialize home directory!", ex);
            myEx.initCause(ex);
            logger.log(Level.SEVERE, "Unable to initialize home directory!", myEx);
            throw ex;
        }
    }
    
    private static void loadDatabasesFromFile() throws IOException {
        try {
            FileObject json = home.getFileObject(ORACLE_HOME, JSON_FILE);
            databases = json==null? new TreeSet<OracleDatabase>() : new OracleJsonReader(json).loadDatabases();
        } catch (IOException ex) {
            IOException myEx = new IOException("Unable to load json file!", ex);
            myEx.initCause(ex);
            logger.log(Level.SEVERE, "Unable to load json file!", myEx);
            throw ex;
        }
    }
    
    static boolean openDatabase(OracleDatabase db) throws Exception {
        try {
            if(databases == null)
                loadDatabases();
            db.setOpened(true);
            saveDatabases(new ArrayList<OracleDatabase>(databases));
            logger.log(Level.INFO, "Database '%s' opened.", db.getName());
            return true;
        } catch (Exception ex) {
            IOException myEx = new IOException("Unable to open database!", ex);
            myEx.initCause(ex);
            logger.log(Level.SEVERE, "Unable to open database!", myEx);
            throw ex;
        }
    }
    
    static void addDatabase(OracleDatabase db) throws Exception {
        if(databases == null)
            loadDatabases();
        saveDatabases(db);
        databases.add(db);
        logger.log(Level.INFO, "Database added: %s", db.getName());
    }
    
    static void saveDatabases() throws Exception {
        saveDatabases(new ArrayList<OracleDatabase>(databases));
    }
    
    private static void saveDatabases(OracleDatabase db) throws Exception {
        List<OracleDatabase> dbs = new ArrayList<OracleDatabase>(databases);
        dbs.add(db);
        saveDatabases(dbs);
    }
    
    private static void saveDatabases(List<OracleDatabase> dbs) throws Exception {
        try {
            FileObject json = getOrCreateJsonFile();
            new OracleJsonWriter(json).writeDatabases(dbs);
        } catch (Exception ex) {
            IOException myEx = new IOException("Unable to save json!", ex);
            myEx.initCause(ex);
            logger.log(Level.SEVERE, "Unable to save json!", myEx);
            throw ex;
        }
    }
    
    private static FileObject getOrCreateJsonFile() throws IOException {
        FileObject json = home.getFileObject(ORACLE_HOME, JSON_FILE);
        if(json == null)
            json = home.createData(ORACLE_HOME, JSON_FILE);
        return json;
    }
    
    static boolean closeDatabase(OracleDatabase db) throws Exception {
        try {
            if(databases == null)
                loadDatabases();
            db.setOpened(false);
            saveDatabases(new ArrayList<OracleDatabase>(databases));
            logger.log(Level.INFO, "Database '%s' closed.", db.getName());
            return true;
        } catch (Exception ex) {
            IOException myEx = new IOException("Unable to close database!", ex);
            myEx.initCause(ex);
            logger.log(Level.SEVERE, "Unable to close database!", myEx);
            throw ex;
        }
    }
    
    static boolean deleteDatabase(OracleDatabase db) throws Exception {
        try {
            if(databases == null)
                loadDatabases();
            if(databases.remove(db))
                saveDatabases(new ArrayList<OracleDatabase>(databases));
            logger.log(Level.INFO, "Database '%s' deleted.", db.getName());
            return true;
        } catch (Exception ex) {
            IOException myEx = new IOException("Unable to delete database!", ex);
            myEx.initCause(ex);
            logger.log(Level.SEVERE, "Unable to delete database!", myEx);
            throw ex;
        }
    }

}
