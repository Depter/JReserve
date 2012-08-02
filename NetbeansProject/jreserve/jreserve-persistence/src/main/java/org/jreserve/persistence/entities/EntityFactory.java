package org.jreserve.persistence.entities;

import java.util.HashSet;
import java.util.Set;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "CTL_EntityResolverDisplayName=Entity descriptors"
})
@MIMEResolver.Registration(
    displayName="#CTL_EntityResolverDisplayName",
    resource="EntityResolver.xml"
)
public class EntityFactory {
    
    private final static Logger logger = Logging.getLogger(EntityFactory.class.getName());
    private final static String MIME_TYPE = "jreserve/entity";
    
    private Set<Class<?>> entities = new HashSet<Class<?>>();
    private ClassLoader classLoader = Lookup.getDefault().lookup(ClassLoader.class);
    
    public Set<Class<?>> getEntityClasses() {
        FileObject dir = FileUtil.getConfigFile(EntityRegistrationProcessor.ENTITY_DIRECTORY);
        processDirectory(dir);
        return entities;
    }
    
    private void processDirectory(FileObject dir) {
        if(dir != null && dir.isFolder()) {
            processFiles(dir.getChildren());
        } else {
            logger.warn("Entity directory '%s' not found!", 
                    EntityRegistrationProcessor.ENTITY_DIRECTORY);
        }
    }
    
    private void processFiles(FileObject[] files) {
        for(FileObject file : files)
            if(isEntityFile(file))
                addEntity(file);
    }
    
    private boolean isEntityFile(FileObject file) {
        return MIME_TYPE.equals(file.getMIMEType()) &&
               file.getAttribute(EntityRegistrationProcessor.CLASS_ATRIBUTE) != null;
    }
    
    private void addEntity(FileObject file) {
        logger.info("Processing entity file '%s'.", file.getPath());
        String className = (String) file.getAttribute(EntityRegistrationProcessor.CLASS_ATRIBUTE);
        try {
            entities.add(classLoader.loadClass(className));
        } catch (ClassNotFoundException ex) {
            logger.error(ex, "Unable to load entity class '%s' from file '%s'!", 
                className, file.getPath());
        }
    }
}
