package org.jreserve.audit.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.audit.Auditor;
import static org.jreserve.audit.util.AuditorRegistrationProcessor.ENTITY_DIRECTORY;
import static org.jreserve.audit.util.AuditorRegistrationProcessor.PRIORITY;
import org.jreserve.project.system.ProjectElementFactory;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 */
public class AuditorRegistry {

    private final static Logger logger = Logger.getLogger(AuditorRegistry.class.getName());
    
    private final static Comparator<FileObject> AUDITOR_FILE_COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject f1, FileObject f2) {
            int p1 = getPriority(f1);
            int p2 = getPriority(f2);
            return p1-p2;
        }
        
        private int getPriority(FileObject file) {
            Integer value = (Integer) file.getAttribute(PRIORITY);
            return value==null? Integer.MAX_VALUE : value;
        }
    };
    
    private static List<Auditor> auditors;
    
    public synchronized static List<Auditor> getAuditors(Object value) {
        List<Auditor> result = new ArrayList<Auditor>();
        for(Auditor auditor : getAuditors())
            if(auditor.isInterested(value))
                result.add(auditor);
        return result;
    }
    
    private static List<Auditor> getAuditors() {
        if(auditors == null)
            initializeFactories();
        return auditors;
    }
    
    private static void initializeFactories() {
        logger.log(Level.FINE, "Loading Auditors from \"{0}\"...", ENTITY_DIRECTORY);
        auditors = new ArrayList<Auditor>();
        for(FileObject file : getAuditorFiles())
            loadAuditor(file);
    }
    
    private static FileObject[] getAuditorFiles() {
        FileObject home = FileUtil.getConfigFile(ENTITY_DIRECTORY);
        FileObject[] children = home.getChildren();
        Arrays.sort(children, AUDITOR_FILE_COMPARATOR);
        return children;
    }
    
    private static void loadAuditor(FileObject file) {
        try {
            DataObject data = DataObject.find(file);
            InstanceCookie cookie = data.getLookup().lookup(InstanceCookie.class);
            Auditor auditor = (Auditor) cookie.instanceCreate();
            logger.log(Level.FINE, "Loaded Auditor: {0}", auditor.getClass().getName());
            auditors.add(auditor);
        } catch (Exception ex) {
            logger.log(Level.WARNING, String.format("Unable to load Auditor from file: %s", file.getPath()), ex);
        } 
    }
    
}
