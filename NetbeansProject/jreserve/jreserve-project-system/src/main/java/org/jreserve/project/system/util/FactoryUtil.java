package org.jreserve.project.system.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.project.system.ProjectElementFactory;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.cookies.InstanceCookie;
import org.openide.loaders.DataObject;
import static org.jreserve.project.system.util.ProjectElementFactoryRegistrationProcessor.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactoryUtil {
    
    private final static Logger logger = Logging.getLogger(FactoryUtil.class.getName());
    
    private final static Comparator<FileObject> FACTORY_FILE_COMPARATOR = new Comparator<FileObject>() {
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
    
    private static List<ProjectElementFactory> factories = null;
    
    public static synchronized void loadFactories() {
        if(factories != null)
            return;
        initializeFactories();
    }
    
    private static void initializeFactories() {
        logger.info("Loading ProjectElementFactories from '%s'...", ENTITY_DIRECTORY);
        factories = new ArrayList<ProjectElementFactory>();
        for(FileObject file : getFactoryFiles())
            loadFactory(file);
    }
    
    private static FileObject[] getFactoryFiles() {
        FileObject home = FileUtil.getConfigFile(ENTITY_DIRECTORY);
        FileObject[] children = home.getChildren();
        Arrays.sort(children, FACTORY_FILE_COMPARATOR);
        return children;
    }
    
    private static void loadFactory(FileObject file) {
        try {
            DataObject data = DataObject.find(file);
            InstanceCookie cookie = data.getLookup().lookup(InstanceCookie.class);
            ProjectElementFactory factory = (ProjectElementFactory) cookie.instanceCreate();
            logger.info("Loaded ProjectElementFactory: %s", factory.getClass().getName());
            factories.add(factory);
        } catch (Exception ex) {
            logger.warn(ex, "Unable to load ProjectElementFactory from file: %s", file.getPath());
        } 
    }
    
    public static synchronized List<ProjectElementFactory> getInterestedFactories(Object value) {
        loadFactories();
        List<ProjectElementFactory> result = new ArrayList<ProjectElementFactory>(factories.size());
        for(ProjectElementFactory factory : factories)
            if(factory.isInterested(value))
                result.add(factory);
        return result;
    }
}
