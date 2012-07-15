package org.jreserve.derbydatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.derbydatabase.file.DerbyJsonReader;
import org.jreserve.derbydatabase.file.DerbyJsonWriter;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DerbyHome {
    
    private final static Logger logger = Logger.getLogger(DerbyHome.class.getName());
    
    private final static String DERBY_HOME = "derbydbs"; 
    private final static String JSON_FILE = "json";
    
    private static Set<DerbyDatabase> databases = null;
    private static FileObject home;
   
    static List<DerbyDatabase> getDatabases() throws IOException {
        if(databases == null)
            loadDatabases();
        return new ArrayList<DerbyDatabase>(databases);
    }
   
    private static void loadDatabases() throws IOException {
        initHomeDirectory();
        loadDatabasesFromFile();
    }
   
    private static void initHomeDirectory() throws IOException {
        try {
            FileObject configHome = FileUtil.getConfigRoot();
            home = FileUtil.createFolder(configHome, DERBY_HOME);
            logger.log(Level.CONFIG, "Derby home directory set to: %s", home);
        } catch (IOException ex) {
            IOException myEx = new IOException("Unable to initialize home directory!", ex);
            myEx.initCause(ex);
            logger.log(Level.SEVERE, "Unable to initialize home directory!", myEx);
            throw ex;
        }
    }
    
    private static void loadDatabasesFromFile() throws IOException {
        try {
            FileObject json = home.getFileObject(DERBY_HOME, JSON_FILE);
            databases = json==null? new TreeSet<DerbyDatabase>() : new DerbyJsonReader(json).loadDatabases();
        } catch (IOException ex) {
            IOException myEx = new IOException("Unable to load json file!", ex);
            myEx.initCause(ex);
            logger.log(Level.SEVERE, "Unable to load json file!", myEx);
            throw ex;
        }
    }
    
    static void addDatabase(DerbyDatabase db) throws Exception {
        if(databases == null)
            loadDatabases();
        saveDatabases(db);
        databases.add(db);
        logger.log(Level.INFO, "Database added: %s", db.getPath());
    }
    
    static void saveDatabases() throws Exception {
        saveDatabases(new ArrayList<DerbyDatabase>(databases));
    }
    
    private static void saveDatabases(DerbyDatabase db) throws Exception {
        List<DerbyDatabase> dbs = new ArrayList<DerbyDatabase>(databases);
        dbs.add(db);
        saveDatabases(dbs);
    }
    
    private static void saveDatabases(List<DerbyDatabase> dbs) throws Exception {
        try {
            FileObject json = getOrCreateJsonFile();
            new DerbyJsonWriter(json).writeDatabases(dbs);
        } catch (Exception ex) {
            IOException myEx = new IOException("Unable to save json!", ex);
            myEx.initCause(ex);
            logger.log(Level.SEVERE, "Unable to save json!", myEx);
            throw ex;
        }
    }
    
    private static FileObject getOrCreateJsonFile() throws IOException {
        FileObject json = home.getFileObject(DERBY_HOME, JSON_FILE);
        if(json == null)
            json = home.createData(DERBY_HOME, JSON_FILE);
        return json;
    }
    
    static boolean closeDatabase(DerbyDatabase db) throws Exception {
        try {
            if(databases == null)
                loadDatabases();
            if(databases.remove(db))
                saveDatabases(new ArrayList<DerbyDatabase>(databases));
            logger.log(Level.INFO, "Database '%s' closed.", db.getPath());
            return true;
        } catch (Exception ex) {
            IOException myEx = new IOException("Unable to close database!", ex);
            myEx.initCause(ex);
            logger.log(Level.SEVERE, "Unable to close database!", myEx);
            throw ex;
        }
    }
    
    static boolean deleteDatabase(DerbyDatabase db) throws Exception {
        if(!deleteDatabaseFile(db.getPath())) {
            IOException ex = new IOException("Unable to delete file: "+db.getPath());
            //ex.fillInStackTrace();
            logger.log(Level.SEVERE, "Unable to delete database!", ex);
            throw ex;
        }
        return closeDatabase(db);
    }
    
    private static boolean deleteDatabaseFile(String path) {
        File db = path == null? null : new File(path);
        if(db == null || !db.exists())
            return true;
        return deleteFile(db);
    }
    
    private static boolean deleteFile(File file) {
        if(file.isDirectory()) {
            for(File child : file.listFiles())
                if(!deleteFile(child))
                    return false;
        }
        return file.delete();
    }
}
